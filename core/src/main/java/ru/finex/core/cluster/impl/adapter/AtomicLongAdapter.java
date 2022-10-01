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

}
