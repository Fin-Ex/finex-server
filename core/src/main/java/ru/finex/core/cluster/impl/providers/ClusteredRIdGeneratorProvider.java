package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RIdGenerator;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author oracle
 */
public class ClusteredRIdGeneratorProvider implements ClusteredProvider<RIdGenerator> {

    @Override
    public RIdGenerator get(Type type, RedissonClient client, String name) {
        return client.getIdGenerator(name);
    }

}
