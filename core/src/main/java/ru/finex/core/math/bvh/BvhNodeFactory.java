package ru.finex.core.math.bvh;

import ru.finex.core.math.bv.BoundingVolume;

/**
 * @author oracle
 */
public interface BvhNodeFactory {

    BvhNode<?, ?> createBvhNode(BoundingVolume<?> boundingVolume);

}
