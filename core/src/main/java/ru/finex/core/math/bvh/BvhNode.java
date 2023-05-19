package ru.finex.core.math.bvh;

import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.vector.FloatMathVector;
import ru.finex.core.pool.Cleanable;

/**
 * @author oracle
 */
public interface BvhNode<VectorType extends FloatMathVector<VectorType>,
    BoundingVolumeType extends BoundingVolume<VectorType>> extends Cleanable {

    BoundingVolumeType getBoundingVolume();

}
