package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingDeque;

/**
 * @author m0nster.mind
 */
public class ClusteredBlockingDequeProvider implements ClusteredProvider<BlockingDeque> {

    @Override
    public BlockingDeque get(Type type, RedissonClient client, String name) {
        return client.getBlockingDeque(name);
    }

}
