package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.Map;
import ru.finex.core.cluster.impl.adapter.MapAdapter;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredMapProvider implements ClusteredProvider<Map> {

    @Override
    public Map get(Type type, RedissonClient client, String name) {
        return new MapAdapter(client.getMap(name));
    }

}
