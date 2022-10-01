package ru.finex.core.cluster.impl.adapter;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.redisson.api.RAtomicLong;
import ru.finex.core.cluster.AtomicLong;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class AtomicLongAdapter implements AtomicLong {

    @Delegate(types = AtomicLong.class)
    private final RAtomicLong value;

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
