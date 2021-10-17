package ru.finex.core.cluster;

import org.redisson.api.RedissonClient;

/**
 * @author m0nster.mind
 */
public interface ClusterService {

    /**
     * Redisson client.
     * @return {@link RedissonClient}
     */
    RedissonClient getClient();

}
