package ru.finex.core.cluster.impl.providers;

import java.lang.reflect.Type;
import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;

/**
 * @author oracle
 */
public class ClusteredRIdGeneratorProvider implements ClusteredProvider<RIdGenerator> {

    @Override
    public RIdGenerator get(Type type, RedissonClient client, String name) {
        return client.getIdGenerator(name);
    }

}
