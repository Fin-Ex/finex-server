package ru.finex.core.cluster.impl.adapter;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import ru.finex.core.cluster.AtomicInteger;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class AtomicIntegerAdapter implements AtomicInteger {

    private final RAtomicLong value;

    @Override
    public int getAndDecrement() {
        return (int) value.getAndDecrement();
    }

    @Override
    public int addAndGet(int delta) {
        return (int) value.addAndGet(delta);
    }

    @Override
    public boolean compareAndSet(int expect, int update) {
        return value.compareAndSet(expect, update);
    }

    @Override
    public int decrementAndGet() {
        return (int) value.decrementAndGet();
    }

    @Override
    public int get() {
        return (int) value.get();
    }

    @Override
    public int getAndAdd(int delta) {
        return (int) value.getAndAdd(delta);
    }

    @Override
    public int getAndSet(int newValue) {
        return (int) value.getAndSet(newValue);
    }

    @Override
    public int incrementAndGet() {
        return (int) value.incrementAndGet();
    }

    @Override
    public int getAndIncrement() {
        return (int) value.getAndIncrement();
    }

    @Override
    public void set(int newValue) {
        value.set(newValue);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(obj);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
