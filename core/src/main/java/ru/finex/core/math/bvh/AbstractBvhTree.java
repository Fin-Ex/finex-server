package ru.finex.core.math.bvh;

import com.google.inject.TypeLiteral;
import org.apache.commons.pool2.ObjectPool;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.vector.FloatMathVector;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.PooledObject;

/**
 * @author m0nster.mind
 * @author oracle
 */
public abstract class AbstractBvhTree<VectorType extends FloatMathVector<VectorType>,
    BoundingVolumeType extends BoundingVolume<VectorType>> implements BvhTree<VectorType, BoundingVolumeType> {

    public static final int DEFAULT_PRECISION = 100;

    protected final int precision;

    protected final ThreadLocal<ObjectPool<BvhNode<VectorType, BoundingVolumeType>>> localNodePool;

    protected int count;

    protected int leafs;

    protected int root;

    protected BvhNode<VectorType, BoundingVolumeType>[] nodes = new BvhNode[0];

    /**
     * Create new BVH-tree with {@link #DEFAULT_PRECISION default node precision}.
     * @param poolService pool service to create node pool
     * @param pooledObject pool configuration
     */
    protected AbstractBvhTree(PoolService poolService, PooledObject pooledObject) {
        this(DEFAULT_PRECISION, poolService, pooledObject);
    }

    /**
     * Create new BVH-tree with specified node precision.
     *
     * @param precision node precision
     * @param poolService pool service to create node pool
     * @param pooledObject pool configuration
     */
    protected AbstractBvhTree(int precision, PoolService poolService, PooledObject pooledObject) {
        this.precision = precision;

        TypeLiteral<BvhNode<VectorType, BoundingVolumeType>> type = new TypeLiteral<>() {};
        localNodePool = ThreadLocal.withInitial(() -> poolService
            .createDynamicPool((Class<BvhNode<VectorType,BoundingVolumeType>>) type.getRawType(), pooledObject));
    }

    protected void fastClear() {
        count = leafs = 0;
    }

    protected void resize(int size) {
        if (size < nodes.length) {
            return;
        }

        final BvhNode<VectorType, BoundingVolumeType>[] nodes = new BvhNode[size];
        System.arraycopy(this.nodes, 0, nodes, 0, this.nodes.length);

        ObjectPool<BvhNode<VectorType, BoundingVolumeType>> nodePool = localNodePool.get();
        try {
            for (int i = this.nodes.length; i < nodes.length; i++) {
                nodes[i] = nodePool.borrowObject();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.nodes = nodes;
    }

}
