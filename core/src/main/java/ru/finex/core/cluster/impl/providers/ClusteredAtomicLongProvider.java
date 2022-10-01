package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.AtomicLong;
import ru.finex.core.cluster.impl.adapter.AtomicLongAdapter;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredAtomicLongProvider implements ClusteredProvider<AtomicLong> {

    @Override
    public AtomicLong get(Type type, RedissonClient client, String name) {
        return new AtomicLongAdapter(client.getAtomicLong(name));
    }

}
