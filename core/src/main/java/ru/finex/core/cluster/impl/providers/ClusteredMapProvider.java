package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author m0nster.mind
 */
public class ClusteredMapProvider implements ClusteredProvider<Map> {

    @Override
    public Map get(Type type, RedissonClient client, String name) {
        return client.getMap(name);
    }

}
