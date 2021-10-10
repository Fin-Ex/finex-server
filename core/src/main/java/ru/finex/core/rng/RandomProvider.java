package ru.finex.core.rng;

import java.security.SecureRandom;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * @author m0nster.mind
 */
public class RandomProvider<T extends RandomGenerator> {

    private static final RandomProvider<Random> DEFAULT_RNG = new RandomProvider<>(ThreadLocal.withInitial(Random::new));
    private static final RandomProvider<SecureRandom> SECURE_RNG = new RandomProvider<>(ThreadLocal.withInitial(SecureRandom::new));

    public static RandomProvider<Random> defaultRandom() {
        return DEFAULT_RNG;
    }

    public static RandomProvider<SecureRandom> secureRandom() {
        return SECURE_RNG;
    }

    private final ThreadLocal<T> local;

    private RandomProvider(ThreadLocal<T> local) {
        this.local = local;
    }

    public T get() {
        return local.get();
    }

}
