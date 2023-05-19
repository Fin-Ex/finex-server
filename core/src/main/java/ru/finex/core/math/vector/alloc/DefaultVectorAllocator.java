package ru.finex.core.math.vector.alloc;

import ru.finex.core.math.vector.FloatMathVector;

/**
 * Default vector allocator.
 *
 * @param <T> vector type
 * @author m0nster.mind
 */
public abstract class DefaultVectorAllocator<T extends FloatMathVector<T>> implements VectorAllocator<T> {

    @Override
    public final void free(T vector) {
        // none
    }

}
