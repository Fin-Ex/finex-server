package ru.finex.core.math;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author oracle
 */
@Data
@RequiredArgsConstructor
public class Ray2f {

    @Getter
    private final Vector2f start;

    @Getter
    private final Vector2f direction;

    public Ray2f(VectorAllocator<Vector2f> allocator) {
        start = allocator.alloc();
        direction = allocator.alloc();
    }

    /**
     * Copy coordinates from start vector to ray start point.
     * @param start start vector
     * @return this
     */
    public Ray2f setStart(Vector2f start) {
        this.start.set(start);
        return this;
    }

    /**
     * Copy direction from direction vector to ray direction.
     * @param direction direction vector
     * @return this
     */
    public Ray2f setDirection(Vector2f direction) {
        this.direction.set(direction);
        return this;
    }

}
