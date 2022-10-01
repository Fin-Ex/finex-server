package ru.finex.core.cluster;

/**
 * @author m0nster.mind
 */
public interface AtomicInteger {

    /**
     * Atomically decrements by one the current value.
     *
     * @return the previous value
     */
    int getAndDecrement();

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the updated value
     */
    int addAndGet(int delta);

    /**
     * Atomically sets the value to the given updated value
     * only if the current value {@code ==} the expected value.
     *
     * @param expect the expected value
     * @param update the new value
     * @return true if successful; or false if the actual value
     *         was not equal to the expected value.
     */
    boolean compareAndSet(int expect, int update);

    /**
     * Atomically decrements the current value by one.
     *
     * @return the updated value
     */
    int decrementAndGet();

    /**
     * Returns current value.
     *
     * @return the current value
     */
    int get();

    /**
     * Atomically adds the given value to the current value.
     *
     * @param delta the value to add
     * @return the old value before the add
     */
    int getAndAdd(int delta);

    /**
     * Atomically sets the given value and returns the old value.
     *
     * @param newValue the new value
     * @return the old value
     */
    int getAndSet(int newValue);

    /**
     * Atomically increments the current value by one.
     *
     * @return the updated value
     */
    int incrementAndGet();

    /**
     * Atomically increments the current value by one.
     *
     * @return the old value
     */
    int getAndIncrement();

    /**
     * Atomically sets the given value.
     *
     * @param newValue the new value
     */
    void set(int newValue);

}
