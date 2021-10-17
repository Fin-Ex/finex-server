package ru.finex.core.cluster.impl;

import org.redisson.api.RBucket;
import ru.finex.core.utils.Holder;

/**
 * @param value stored value
 * @param <T> stored class
 * @author m0nster.mind
 */
public record BucketAdapter<T>(RBucket<T> value) implements Holder<T> {

    @Override
    public void set(T value) {
        this.value.set(value);
    }

    @Override
    public T get() {
        return value.get();
    }

}
