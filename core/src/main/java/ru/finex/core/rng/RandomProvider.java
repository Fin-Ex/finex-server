package ru.finex.core.rng;

/**
 * @param <T> random generator type
 * @author m0nster.mind
 */
public interface RandomProvider<T extends RandomGenerator> {

    /**
     * Provides a random generator.
     * @return random generator
     */
    T get();

}
