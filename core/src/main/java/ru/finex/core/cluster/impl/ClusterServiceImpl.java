package ru.finex.core.cluster.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import ru.finex.core.cluster.ClusterService;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClusterServiceImpl implements ClusterService {

    @Getter
    private final RedissonClient client;

    @PreDestroy
    private void destroy() {
        client.shutdown();
    }

}
