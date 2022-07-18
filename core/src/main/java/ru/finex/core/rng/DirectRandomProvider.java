package ru.finex.core.rng;

import java.util.function.Supplier;

/**
 * @param <T> random generator type
 * @author m0nster.mind
 */
public final class DirectRandomProvider<T extends RandomGenerator> implements RandomProvider<T> {

    private final T reference;

    public DirectRandomProvider(Supplier<T> rngSupplier) {
        this.reference = rngSupplier.get();
    }

    @Override
    public T get() {
        return reference;
    }

}
