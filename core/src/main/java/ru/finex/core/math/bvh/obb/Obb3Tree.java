package ru.finex.core.math.bvh.obb;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collection;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.impl.OrientedBox3f;
import ru.finex.core.math.bvh.AbstractBvhTree;
import ru.finex.core.math.bvh.BvhElement;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

/**
 * @author oracle
 */
public class Obb3Tree extends AbstractBvhTree<Vector3f, OrientedBox3f> {

    public Obb3Tree(PoolService poolService, PooledObject pooledObject) {
        super(poolService, pooledObject);
    }

    public Obb3Tree(int precision, PoolService poolService, PooledObject pooledObject) {
        super(precision, poolService, pooledObject);
    }

    @Override
    public void build(Collection<BvhElement<Vector3f, OrientedBox3f>> bvhElements) {

    }

    @Override
    public void insert(BvhElement<Vector3f, OrientedBox3f> element) {

    }

    @Override
    public IntList query(BoundingVolume<?> boundingVolume) {
        return null;
    }

    @Override
    public IntList queryCircle(Vector3f point, float radius) {
        return null;
    }

    @Override
    public IntList queryBoundingVolume(OrientedBox3f boundingVolume) {
        return null;
    }

    @Override
    public void clear() {

    }

}
