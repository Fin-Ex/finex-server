package ru.finex.core.math.vector;

import lombok.experimental.UtilityClass;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author oracle
 */
@UtilityClass
public class VectorUtils {

    /**
     * Cast specified {@literal vector2f} to 3-dimensional vector.
     * Result 3D vector always is (x, 0.0, y).
     * @param vector2f a vector for upcasting op.
     * @param allocator an allocator that will be used for allocations in scope of this operation.
     * @return a new {@link Vector3f}.
     * */
    public Vector3f upcast(Vector2f vector2f, VectorAllocator<Vector3f> allocator) {
        return allocator.alloc().set(vector2f.getX(), 0, vector2f.getY());
    }

    /**
     * Cast specified {@literal vector3f} to 2-dimensional vector.
     * Result 2D vector always is (x, z), in other words - Y coordinate will be ignored.
     * @param vector3f a vector for downcasting op.
     * @param allocator an allocator that will be used for allocations in scope of this operation.
     * @return a new {@link Vector2f}.
     * */
    public Vector2f downcast(Vector3f vector3f, VectorAllocator<Vector2f> allocator) {
        return allocator.alloc().set(vector3f.getX(), vector3f.getZ());
    }

}
