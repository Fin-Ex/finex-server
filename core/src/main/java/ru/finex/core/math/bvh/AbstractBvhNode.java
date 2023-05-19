package ru.finex.core.math.bvh;

import lombok.Getter;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.vector.FloatMathVector;

/**
 * @author oracle
 */
public abstract class AbstractBvhNode<VectorType extends FloatMathVector<VectorType>,
    BoundingVolumeType extends BoundingVolume<VectorType>> implements BvhNode<VectorType, BoundingVolumeType> {

    @Getter
    private final BoundingVolumeType boundingVolume;

    protected int value;

    protected int parent = -1;

    protected int left = -1;

    protected int right = -1;

    protected AbstractBvhNode(BoundingVolumeType boundingVolume) {
        this.boundingVolume = boundingVolume;
    }

    @Override
    public void clear() {
        getBoundingVolume().dispose();
        value = 0;
        parent = left = right = -1;
    }

}
