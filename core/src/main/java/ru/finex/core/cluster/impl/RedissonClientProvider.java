package ru.finex.core.cluster.impl;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RedissonClientProvider implements Provider<RedissonClient> {

    private final Config config;

    @Override
    public RedissonClient get() {
        return Redisson.create(config);
    }
}
