package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RedissonClient;
import ru.finex.core.events.cluster.ClusterEventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredEventBusProvider implements ClusteredProvider<ClusterEventBus> {

    @Override
    public ClusterEventBus get(Type type, RedissonClient client, String name) {
        return new ClusterEventBus(
            client.getTopic(name),
            (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]
        );
    }

}
