package ru.finex.core.rng;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class RandomGeneratorDecorator implements RandomGenerator {

    @Delegate
    private final java.util.random.RandomGenerator rng;

    @Override
    public java.util.random.RandomGenerator unwrap() {
        return rng;
    }
}
