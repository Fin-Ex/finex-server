package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.Queue;

/**
 * @author m0nster.mind
 */
public class ClusteredQueueProvider implements ClusteredProvider<Queue> {

    @Override
    public Queue get(Type type, RedissonClient client, String name) {
        return client.getQueue(name);
    }

}
