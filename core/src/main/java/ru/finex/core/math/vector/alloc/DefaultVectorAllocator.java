package ru.finex.core.math.vector.alloc;

import ru.finex.core.math.vector.MathVector;
import ru.finex.core.math.vector.VectorAllocator;

/**
 * Default vector allocator.
 *
 * @param <T> vector type
 * @author m0nster.mind
 */
public abstract class DefaultVectorAllocator<T extends MathVector> implements VectorAllocator<T> {

    @Override
    public final void free(T vector) {
        // none
    }

}
