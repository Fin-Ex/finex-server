package ru.finex.core.cluster;

import org.redisson.api.RedissonClient;

/**
 * @author m0nster.mind
 */
public interface ClusterService {

    RedissonClient getClient();

}
