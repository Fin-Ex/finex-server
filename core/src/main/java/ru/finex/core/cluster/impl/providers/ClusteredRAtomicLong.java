package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredRAtomicLong implements ClusteredProvider<RAtomicLong> {

    @Override
    public RAtomicLong get(Type type, RedissonClient client, String name) {
        return client.getAtomicLong(name);
    }

}
