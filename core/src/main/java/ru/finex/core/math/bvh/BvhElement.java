package ru.finex.core.math.bvh;

import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.vector.FloatMathVector;

/**
 * @author m0nster.mind
 * @author oracle
 */
public interface BvhElement<VectorType extends FloatMathVector<VectorType>,
    BoundingVolumeType extends BoundingVolume<VectorType>> {

    /**
     * Unique ID to store in BVH.
     *
     * @return ID
     */
    int getId();

    /**
     * Bounding volume of bvh element.
     *
     * @return {@link BoundingVolume} instance.
     */
    BoundingVolumeType getBoundingVolume();

}
