package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author m0nster.mind
 */
public class ClusteredRWLockProvider implements ClusteredProvider<ReadWriteLock> {

    @Override
    public ReadWriteLock get(Type type, RedissonClient client, String name) {
        return client.getReadWriteLock(name);
    }

}
