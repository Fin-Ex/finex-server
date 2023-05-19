package ru.finex.core.math.bv;

import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author oracle
 */
public interface BoundingVolume3 extends BoundingVolume<Vector3f> {

    /**
     * Test shape to contain specified point inside.
     * @param x coordinate of point on X-axis.
     * @param y coordinate of point on Y-axis.
     * @param z coordinate of point on Z-axis.
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(float x, float y, float z);

    /**
     * Move bounding volume center to specified point.
     * @param x coordinate of point on X-axis.
     * @param y coordinate of point on Y-axis.
     * @param z coordinate of point on Z-axis.
     * */
    void moveCenter(float x, float y, float z);

    /**
     * Tests intersection with 3-dimensional bounding volume.
     *
     * @param boundingVolume a bounding volume for intersection test.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with aabb, otherwise {@literal false}.
     * */
    boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator);

}
