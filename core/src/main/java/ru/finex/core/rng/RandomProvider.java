package ru.finex.core.rng;

import java.security.SecureRandom;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * @param <T> random type
 * @author m0nster.mind
 */
public final class RandomProvider<T extends RandomGenerator> {

    private static final RandomProvider<Random> DEFAULT_RNG = new RandomProvider<>(ThreadLocal.withInitial(Random::new));
    private static final RandomProvider<SecureRandom> SECURE_RNG = new RandomProvider<>(ThreadLocal.withInitial(SecureRandom::new));

    private final ThreadLocal<T> local;

    private RandomProvider(ThreadLocal<T> local) {
        this.local = local;
    }

    /**
     * Default random provider.
     * @return random provider
     */
    public static RandomProvider<Random> defaultRandom() {
        return DEFAULT_RNG;
    }

    /**
     * Secure random provider.
     * @return random provider
     */
    public static RandomProvider<SecureRandom> secureRandom() {
        return SECURE_RNG;
    }

    /**
     * Provides a random generator.
     * @return random generator
     */
    public T get() {
        return local.get();
    }

}
