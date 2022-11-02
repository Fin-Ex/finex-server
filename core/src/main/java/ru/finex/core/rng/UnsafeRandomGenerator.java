package ru.finex.core.rng;

import java.util.Random;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MagicNumber")
public class UnsafeRandomGenerator extends Random implements RandomGenerator {

    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long ADDEND = 0xBL;
    private static final long MASK = (1L << 48) - 1;

    private long seed;

    public UnsafeRandomGenerator(long seed) {
        super(seed);
    }

    public UnsafeRandomGenerator() {
        super();
    }

    @Override
    public void setSeed(long seed) {
        this.seed = initialScramble(seed);
    }

    @Override
    protected int next(int bits) {
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        return (int)(seed >>> (48 - bits));
    }

    @Override
    public double nextGaussian() {
        return super.nextGaussian();
    }

    @Override
    public java.util.random.RandomGenerator unwrap() {
        return this;
    }

    private static long initialScramble(long seed) {
        return (seed ^ MULTIPLIER) & MASK;
    }
}
