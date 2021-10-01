package ru.finex.core.cluster.impl.providers;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;

/**
 * @author m0nster.mind
 */
public class ClusteredRBucketProvider implements ClusteredProvider<RBucket> {

    @Override
    public RBucket get(Type type, RedissonClient client, String name) {
        return client.getBucket(name);
    }

}
