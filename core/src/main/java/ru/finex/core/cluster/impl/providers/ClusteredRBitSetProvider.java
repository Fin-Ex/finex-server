package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredRBitSetProvider implements ClusteredProvider<RBitSet> {

    @Override
    public RBitSet get(Type type, RedissonClient client, String name) {
        return client.getBitSet(name);
    }

}
