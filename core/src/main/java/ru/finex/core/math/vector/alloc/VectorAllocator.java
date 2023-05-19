package ru.finex.core.math.vector.alloc;

import ru.finex.core.math.vector.FloatMathVector;

/**
 * Vector allocator.
 *
 * @param <T> vector type
 * @author m0nster.mind
 */
public interface VectorAllocator<T extends FloatMathVector<T>> {

    /**
     * Allocates a new vector.
     * @return vector
     */
    T alloc();

    /**
     * Deallocate vector.
     * @param vector vector
     */
    void free(T vector);

}
