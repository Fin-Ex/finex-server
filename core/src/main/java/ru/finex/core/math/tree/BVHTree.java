package ru.finex.core.math.tree;

import org.apache.commons.pool2.ObjectPool;
import ru.finex.core.math.shape.impl.Box2;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.pool.Cleanable;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:InnerAssignment")
public class BVHTree {

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
    public BVHTree(PoolService poolService, PooledObject pooledObject) {
        this(DEFAULT_PRECISION, poolService, pooledObject);
    }

    /**
     * Create new BVH-tree with specified node precision.
     *
     * @param precision node precision
     * @param poolService pool service to create node pool
     * @param pooledObject pool configuration
     */
    public BVHTree(int precision, PoolService poolService, PooledObject pooledObject) {
        this.precision = precision;
        localNodePool = ThreadLocal.withInitial(() -> poolService.createDynamicPool(Node.class, pooledObject));
    }

    /**
     * Build BVH-tree.
     *
     * @param elements tree elements
     */
    public void build(Collection<BVHTreeElement> elements) {
        fastClear();

        count = elements.size();
        if (count == 0) {
            return;
        }

        resize((count << 1) - 1);

        Iterator<BVHTreeElement> shapeIterator = elements.iterator();
        try {
            for (int i = 0; i < count; i++) {
                Node node = nodes[i];

                BVHTreeElement shapeComponent = shapeIterator.next();
                node.value = shapeComponent.getId();
                node.box.set(shapeComponent.getBoundingBox().toBox2(precision));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        root = bindNodes(0, count);
    }

    private int bindNodes(int start, int end) {
        int remains = end - start;
        if (remains == 1) { // leaf
            leafs++;
            return start;
        }

        int index = count++;
        Node node = nodes[index];
        Box2 box = calculateBox(node, start, end);

        int width = box.getWidth();
        int height = box.getHeight();
        Arrays.sort(nodes, start, end, width > height ? X_CMP : Y_CMP);

        int divider = start + remains / 2;

        node.left = bindNodes(start, divider);
        node.right = bindNodes(divider, end);

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
     * Clear BVH.
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
     * Query to search all objects in specified circle radius.
     *
     * @param point circle center
     * @param radius circle radius
     * @return search result - object ID's
     */
    public List<Integer> queryCircle(Vector3f point, float radius) {
        int x = (int) (point.getX() * precision);
        int y = (int) (point.getZ() * precision);
        int radi = (int) (radius * precision);

        return queryCircle(x, y, radi);
    }

    private List<Integer> queryCircle(int x, int y, int radius) {
        List<Integer> result = new ArrayList<>();
        long sqRadius = (long) radius * radius;

        Queue<Integer> awaitNodes = new LinkedList<>();
        awaitNodes.offer(root);

        for (Integer index = awaitNodes.poll(); index != null; index = awaitNodes.poll()) {
            Node node = nodes[index];

            boolean isHit = node.box.contains(x, y) || node.box.internalIntersects(x, y, sqRadius);
            boolean isLeaf = node.left == -1 && node.right == -1;

            if (isHit && isLeaf) {
                result.add(node.value);
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

        return result;
    }

    @SuppressWarnings("checkstyle:VisibilityModifier")
    public static class Node implements Cleanable {
        public final Box2 box = new Box2();
        public int value;
        public int left = -1;
        public int right = -1;

        public Node() {
        }

        @Override
        public void clear() {
            box.xmax = box.ymax = box.xmin = box.ymin = 0;
            value = 0;
            left = right = -1;
        }
    }

}
