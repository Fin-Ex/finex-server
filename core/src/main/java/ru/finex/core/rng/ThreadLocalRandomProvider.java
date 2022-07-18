package ru.finex.core.rng;

import java.util.function.Supplier;

/**
 * @param <T> random generator type
 * @author m0nster.mind
 */
public final class ThreadLocalRandomProvider<T extends RandomGenerator> implements RandomProvider<T> {

    private final ThreadLocal<T> local;

    public ThreadLocalRandomProvider(Supplier<T> rngSupplier) {
        this.local = ThreadLocal.withInitial(rngSupplier);
    }

    @Override
    public T get() {
        return local.get();
    }

}
