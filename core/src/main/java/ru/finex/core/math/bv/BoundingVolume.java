package ru.finex.core.math.bv;

import ru.finex.core.math.vector.FloatMathVector;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author oracle
 */
public interface BoundingVolume<VectorType extends FloatMathVector<VectorType>> {

    /**
     * Tests is box contains requested point.
     * @param point a point to check for.
     *
     * @return {@literal true} if box contains provided point otherwise {@literal false}.
     * */
    boolean contains(VectorType point, VectorAllocator<VectorType> allocator);

    /**
     * Move obb left-upper corner to specified point.
     * @param point desired left-upper corner point.
     * */
    void move(VectorType point);

    /**
     * Move obb center to specified point.
     * @param point desired center point.
     * */
    void moveCenter(VectorType point);

    /**
     * Expand bounding volume to encapsulate specified point.
     * @param point a point that need to be encapsulated.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * */
    void encapsulate(VectorType point, VectorAllocator<VectorType> allocator);

    /**
     * Expand this bounding volume to encapsulate specified {@literal boundingVolume}.
     *
     * @param boundingVolume a bounding volume to be joined.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * */
    <T extends BoundingVolume<VectorType>> void union(T boundingVolume, VectorAllocator<VectorType> allocator);

    void dispose();
}
