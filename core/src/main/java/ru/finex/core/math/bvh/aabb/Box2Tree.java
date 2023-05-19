package ru.finex.core.math.bvh.aabb;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import lombok.ToString;
import org.apache.commons.pool2.ObjectPool;
import ru.finex.core.math.bv.impl.Box2;
import ru.finex.core.math.bv.impl.Box2f;
import ru.finex.core.math.bv.impl.Circle2f;
import ru.finex.core.math.shape.Shape;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.pool.Cleanable;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

/**
 * 2D AABB-tree.
 *
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:InnerAssignment")
public class Box2Tree {

    public static final int DEFAULT_PRECISION = 100;

    private static final Comparator<Node> X_CMP = (o1, o2) -> o1.box.compareX(o2.box);
    private static final Comparator<Node> Y_CMP = (o1, o2) -> o1.box.compareY(o2.box);

    private final ThreadLocal<ObjectPool<Node>> localNodePool;
    private final int precision;

    private Node[] nodes = new Node[0];

    private int count;
    private int leafs;
    private int root;

    /**
     * Create new BVH-tree with {@link #DEFAULT_PRECISION default node precision}.
     * @param poolService pool service to create node pool
     * @param pooledObject pool configuration
     */
    public Box2Tree(PoolService poolService, PooledObject pooledObject) {
        this(DEFAULT_PRECISION, poolService, pooledObject);
    }

    /**
     * Create new BVH-tree with specified node precision.
     *
     * @param precision node precision
     * @param poolService pool service to create node pool
     * @param pooledObject pool configuration
     */
    public Box2Tree(int precision, PoolService poolService, PooledObject pooledObject) {
        this.precision = precision;
        localNodePool = ThreadLocal.withInitial(() -> poolService.createDynamicPool(Node.class, pooledObject));
    }

    /**
     * Build BVH-tree.
     *
     * @param elements tree elements
     */
    public void build(Collection<Box2TreeElement> elements) {
        fastClear();

        count = elements.size();
        if (count == 0) {
            return;
        }

        resize((count << 1) - 1);

        Iterator<Box2TreeElement> shapeIterator = elements.iterator();
        try {
            for (int i = 0; i < count; i++) {
                Node node = nodes[i];

                Box2TreeElement shapeComponent = shapeIterator.next();
                node.value = shapeComponent.getId();
                node.box.set(shapeComponent.getBoundingBox(), precision);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        root = bindNodes(-1, 0, count);
    }

    private int bindNodes(int parent, int start, int end) {
        int remains = end - start;
        if (remains == 1) { // leaf
            leafs++;
            return start;
        }

        int index = count++;
        Node node = nodes[index];
        node.parent = parent;

        Box2 box = calculateBox(node, start, end);

        int width = box.getWidth();
        int height = box.getHeight();
        Arrays.sort(nodes, start, end, width > height ? X_CMP : Y_CMP);

        int divider = start + remains / 2;

        node.left = bindNodes(index, start, divider);
        node.right = bindNodes(index, divider, end);

        return index;
    }

    private Box2 calculateBox(Node node, int start, int end) {
        Box2 box = node.box;
        for (int i = start + 1; i < end; i++) {
            box.union(nodes[i].box);
        }

        return box;
    }

    /**
     * Insert a new element to tree.
     * Each insertion operation makes tree is more unbalanced.
     *
     * @param element tree element
     */
    public void insert(Box2TreeElement element) {
        count += 2; // leaf and branch
        resize(count);

        Node leafNode = nodes[count - 1];
        leafNode.box.set(element.getBoundingBox(), precision);

        Node branchNode = nodes[count - 2];
        branchNode.right = count - 1;
        branchNode.box.set(leafNode.box);

        if (count == 2) { // tree is empty
            root = 0;
            return;
        }

        // find node split candidate
        int candidateIndex = findCandidate(leafNode.box);
        if (candidateIndex == -1) {
            candidateIndex = root;
        }

        // replace candidate node to new branch node
        Node candidate = nodes[candidateIndex];
        branchNode.box.union(candidate.box);
        branchNode.parent = candidate.parent;
        branchNode.left = candidateIndex;
        candidate.parent = count - 2;

        // resize all parents
        for (int index = candidate.parent; index != -1; ) {
            Node node = nodes[index];
            node.box.union(branchNode.box);
            index = node.parent;
        }
    }

    private int findCandidate(Box2 box) {
        int candidateIndex = -1;

        Queue<Integer> awaitNodes = new LinkedList<>();
        awaitNodes.offer(root);

        for (Integer index = awaitNodes.poll(); index != null; index = awaitNodes.poll()) {
            Node node = nodes[index];

            boolean isHit = node.box.intersects(box);
            boolean isLeaf = node.left == -1 && node.right == -1;

            if (isHit && isLeaf) {
                return index;
            }

            if (isHit) {
                int left = node.left;
                if (left != -1) {
                    awaitNodes.offer(left);
                }

                int right = node.right;
                if (right != -1) {
                    awaitNodes.offer(right);
                }
            }

            if (candidateIndex == -1) {
                candidateIndex = index;
            } else {
                // search smaller candidate
                Node candidate = nodes[candidateIndex];
                int avgBoxSize = (candidate.box.getHeight() + candidate.box.getWidth()) / 2;
                int nAvgBoxSize = (node.box.getHeight() + candidate.box.getWidth()) / 2;
                if (nAvgBoxSize < avgBoxSize) {
                    candidateIndex = index;
                }
            }
        }

        return candidateIndex;
    }

    /**
     * Clear tree.
     * Reset all indices and return nodes into pool.
     */
    public void clear() {
        fastClear();

        ObjectPool<Node> nodePool = localNodePool.get();
        try {
            for (int i = 0; i < nodes.length; i++) {
                nodePool.returnObject(nodes[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        nodes = new Node[0];
    }

    private void fastClear() {
        count = leafs = 0;
    }

    private void resize(int size) {
        if (size < nodes.length) {
            return;
        }

        final Node[] nodes = new Node[size];
        System.arraycopy(this.nodes, 0, nodes, 0, this.nodes.length);

        ObjectPool<Node> nodePool = localNodePool.get();
        try {
            for (int i = this.nodes.length; i < nodes.length; i++) {
                nodes[i] = nodePool.borrowObject();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.nodes = nodes;
    }

    /**
     * Query to search all objects in specified shape.
     *
     * @param shape shape
     * @return search result - object ID's
     */
    public IntList query(Shape shape) {
        IntList result;
        if (shape instanceof Circle2f circle) {
            result = queryCircle(circle.getCenter(), circle.getRadius());
        } else if (shape instanceof Box2f box) {
            result = queryBox(box);
        } else {
            throw new IllegalArgumentException("Unknown shape type: " + shape.getClass().getCanonicalName());
        }

        return result;
    }

    /**
     * Query to search all objects in specified circle radius.
     *
     * @param point circle center
     * @param radius circle radius
     * @return search result - object ID's
     */
    public IntList queryCircle(Vector2f point, float radius) {
        int x = (int) (point.getX() * precision);
        int y = (int) (point.getY() * precision);
        int radi = (int) (radius * precision);

        return queryCircle(x, y, radi);
    }

    private IntList queryCircle(int x, int y, int radius) {
        IntList results = new IntArrayList();
        long sqRadius = (long) radius * radius;

        Queue<Integer> awaitNodes = new LinkedList<>();
        awaitNodes.offer(root);

        for (Integer index = awaitNodes.poll(); index != null; index = awaitNodes.poll()) {
            Node node = nodes[index];

            boolean isHit = node.box.contains(x, y) || node.box.internalIntersects(x, y, sqRadius);
            boolean isLeaf = node.left == -1 && node.right == -1;

            if (isHit && isLeaf) {
                results.add(node.value);
            }

            if (isHit) {
                int left = node.left;
                if (left != -1) {
                    awaitNodes.offer(left);
                }

                int right = node.right;
                if (right != -1) {
                    awaitNodes.offer(right);
                }
            }
        }

        return results;
    }

    /**
     * Query to search all objects in specified aabb space.
     *
     * @param box aabb
     * @return search result - object ID's
     */
    public IntList queryBox(Box2f box) {
        return queryBox(box.toBox2(precision));
    }

    private IntList queryBox(Box2 box) {
        IntList results = new IntArrayList();

        Queue<Integer> awaitNodes = new LinkedList<>();
        awaitNodes.offer(root);

        for (Integer index = awaitNodes.poll(); index != null; index = awaitNodes.poll()) {
            Node node = nodes[index];

            boolean isHit = node.box.intersects(box);
            boolean isLeaf = node.left == -1 && node.right == -1;

            if (isHit && isLeaf) {
                results.add(node.value);
            }

            if (isHit) {
                int left = node.left;
                if (left != -1) {
                    awaitNodes.offer(left);
                }

                int right = node.right;
                if (right != -1) {
                    awaitNodes.offer(right);
                }
            }
        }

        return results;
    }

    @SuppressWarnings("checkstyle:VisibilityModifier")
    @ToString
    public static class Node implements Cleanable {
        public final Box2 box = new Box2();
        public int value;
        public int parent = -1;
        public int left = -1;
        public int right = -1;

        public Node() {
        }

        @Override
        public void clear() {
            box.xmax = box.ymax = box.xmin = box.ymin = 0;
            value = 0;
            parent = left = right = -1;
        }
    }

}
