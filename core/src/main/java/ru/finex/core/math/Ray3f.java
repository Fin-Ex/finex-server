package ru.finex.core.math;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.VectorAllocator;

/**
 * @author m0nster.mind
 * @since wgp
 */
@Data
@RequiredArgsConstructor
public class Ray3f {

    @Getter
    private final Vector3f start;

    @Getter
    private final Vector3f direction;

    public Ray3f(VectorAllocator<Vector3f> allocator) {
        start = allocator.alloc();
        direction = allocator.alloc();
    }

    /**
     * Copy coordinates from start vector to ray start point.
     * @param start start vector
     * @return this
     */
    public Ray3f setStart(Vector3f start) {
        this.start.set(start);
        return this;
    }

    /**
     * Copy direction from direction vector to ray direction.
     * @param direction direction vector
     * @return this
     */
    public Ray3f setDirection(Vector3f direction) {
        this.direction.set(direction);
        return this;
    }

}
