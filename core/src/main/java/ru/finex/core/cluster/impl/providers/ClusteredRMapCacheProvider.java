package ru.finex.core.cluster.impl.providers;

import java.lang.reflect.Type;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

/**
 * @author oracle
 */
public class ClusteredRMapCacheProvider implements ClusteredProvider<RMapCache> {

    @Override
    public RMapCache get(Type type, RedissonClient client, String name) {
        return client.getMapCache(name);
    }

}
