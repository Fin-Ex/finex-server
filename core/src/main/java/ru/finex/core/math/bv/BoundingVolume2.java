package ru.finex.core.math.bv;

import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author oracle
 */
public interface BoundingVolume2 extends BoundingVolume<Vector2f> {

    /**
     * Tests is box contains requested point expressed in coordinated {@literal x} and {@literal y}.
     * @param x coordinate of point on X-axis.
     * @param y coordinate of point on Y-axis.
     *
     * @return {@literal true} if box contains provided point otherwise {@literal false}.
     * */
    boolean contains(float x, float y);

    boolean contains(Vector3f point, VectorAllocator<Vector2f> allocator);

    /**
     * Move bounding volume center to specified point.
     * @param x coordinate of point on X-axis.
     * @param y coordinate of point on Y-axis.
     * */
    void moveCenter(float x, float y);

    void moveCenter(Vector3f point);

    /**
     * Tests intersection with 2-dimensional bounding volume.
     *
     * @param boundingVolume a bounding volume for intersection test.
     * @param allocator an allocator to be used for further vectors allocation in scope of this operation.
     * @return {@literal true} if this obb intersects with aabb, otherwise {@literal false}.
     * */
    boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector2f> allocator);

}
