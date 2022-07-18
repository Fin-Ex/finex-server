package ru.finex.core.rng;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class RandomProviders {

    private static final RandomProvider<RandomGenerator> DEFAULT_RNG = new ThreadLocalRandomProvider<>(UnsafeRandomGenerator::new);
    private static final RandomProvider<RandomGenerator> SECURE_RNG = new ThreadLocalRandomProvider<>(RandomProviders::newSecureRandom);
    private static final RandomProvider<RandomGenerator> SYNC_RNG = new DirectRandomProvider<>(RandomProviders::newSynchronizedRandom);

    /**
     * Default random provider.
     * @return random provider
     */
    public static RandomProvider<RandomGenerator> defaultRandom() {
        return DEFAULT_RNG;
    }

    /**
     * Secure random provider.
     * @return random provider
     */
    public static RandomProvider<RandomGenerator> secureRandom() {
        return SECURE_RNG;
    }

    /**
     * Synchronized random provider.
     * It's shared to full JVM life-cycle.
     * @return random provider
     */
    public static RandomProvider<RandomGenerator> synchronizedRandom() {
        return SYNC_RNG;
    }

    private static RandomGenerator newSynchronizedRandom() {
        return new RandomGeneratorDecorator(new Random());
    }

    private static RandomGenerator newSecureRandom() {
        return new RandomGeneratorDecorator(new SecureRandom());
    }

}
