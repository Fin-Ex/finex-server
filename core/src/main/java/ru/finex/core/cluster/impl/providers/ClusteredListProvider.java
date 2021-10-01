package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author m0nster.mind
 */
public class ClusteredListProvider implements ClusteredProvider<List> {

    @Override
    public List get(Type type, RedissonClient client, String name) {
        return client.getList(name);
    }

}
