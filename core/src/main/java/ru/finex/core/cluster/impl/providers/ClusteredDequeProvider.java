package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.Deque;

/**
 * @author m0nster.mind
 */
public class ClusteredDequeProvider implements ClusteredProvider<Deque> {

    @Override
    public Deque get(Type type, RedissonClient client, String name) {
        return client.getDeque(name);
    }

}
