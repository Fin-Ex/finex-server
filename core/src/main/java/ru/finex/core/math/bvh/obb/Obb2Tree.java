package ru.finex.core.math.bvh.obb;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collection;
import org.apache.commons.pool2.ObjectPool;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.impl.OrientedBox2f;
import ru.finex.core.math.bvh.AbstractBvhTree;
import ru.finex.core.math.bvh.BvhElement;
import ru.finex.core.math.bvh.BvhNode;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

/**
 * @author oracle
 */
public class Obb2Tree extends AbstractBvhTree<Vector2f, OrientedBox2f> {

    public Obb2Tree(PoolService poolService, PooledObject pooledObject) {
        super(poolService, pooledObject);
    }

    public Obb2Tree(int precision, PoolService poolService, PooledObject pooledObject) {
        super(precision, poolService, pooledObject);
    }

    @Override
    public void build(Collection<BvhElement<Vector2f, OrientedBox2f>> bvhElements) {

    }

    @Override
    public void insert(BvhElement<Vector2f, OrientedBox2f> element) {

    }

    @Override
    public IntList query(BoundingVolume<?> boundingVolume) {
        return null;
    }

    @Override
    public IntList queryCircle(Vector2f point, float radius) {
        return null;
    }

    @Override
    public IntList queryBoundingVolume(OrientedBox2f boundingVolume) {
        return null;
    }

    @Override
    public void clear() {
        fastClear();

        ObjectPool<BvhNode<Vector2f, OrientedBox2f>> nodePool = localNodePool.get();
        try {
            for (int i = 0; i < nodes.length; i++) {
                nodePool.returnObject(nodes[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        nodes = new BvhNode[0];
    }

}
