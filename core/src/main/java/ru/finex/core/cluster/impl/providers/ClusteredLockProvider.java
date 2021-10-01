package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.concurrent.locks.Lock;

/**
 * @author m0nster.mind
 */
public class ClusteredLockProvider implements ClusteredProvider<Lock> {

    @Override
    public Lock get(Type type, RedissonClient client, String name) {
        return client.getLock(name);
    }

}
