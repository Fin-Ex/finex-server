package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.AtomicInteger;
import ru.finex.core.cluster.impl.adapter.AtomicIntegerAdapter;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredAtomicIntegerProvider implements ClusteredProvider<AtomicInteger> {

    @Override
    public AtomicInteger get(Type type, RedissonClient client, String name) {
        return new AtomicIntegerAdapter(client.getAtomicLong(name));
    }

}
