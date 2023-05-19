package ru.finex.core.math.vector.alloc;

import java.util.ArrayDeque;
import lombok.RequiredArgsConstructor;
import ru.finex.core.math.vector.FloatMathVector;

/**
 * Cached allocator.
 *
 * @param <T> vector type
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class CachedAllocator<T extends FloatMathVector<T>> implements VectorAllocator<T> {

    private final ArrayDeque<T> deque = new ArrayDeque<>();
    private final VectorAllocator<T> allocator;

    /**
     * Poll a vector from cache, if cache is empty allocates a new vector.
     * @return vector
     */
    @Override
    public T alloc() {
        T vector = deque.poll();
        if (vector == null) {
            vector = allocator.alloc();
        }

        return vector;
    }

    /**
     * Offer vector to cache.
     * @param vector vector
     */
    @Override
    public void free(T vector) {
        deque.offer(vector);
    }

}
