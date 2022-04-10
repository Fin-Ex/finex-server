package ru.finex.core.math.vector;

/**
 * Vector allocator.
 *
 * @param <T> vector type
 * @author m0nster.mind
 */
public interface VectorAllocator<T extends MathVector> {

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
