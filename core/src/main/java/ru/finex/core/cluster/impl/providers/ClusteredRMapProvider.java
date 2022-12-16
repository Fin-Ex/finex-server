package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author oracle
 */
public class ClusteredRMapProvider implements ClusteredProvider<RMap> {

    @Override
    public RMap get(Type type, RedissonClient client, String name) {
        return client.getMap(name);
    }

}
