package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.impl.ClusterServiceImpl;
import ru.finex.core.cluster.impl.ClusteredListener;
import ru.finex.core.cluster.impl.ClusteredProviders;
import ru.finex.core.cluster.impl.RedissonClientProvider;
import ru.finex.core.cluster.impl.RedissonConfigProvider;

/**
 * @author m0nster.mind
 */
public class ClusterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).toProvider(RedissonConfigProvider.class).in(Singleton.class);
        bind(RedissonClient.class).toProvider(RedissonClientProvider.class).in(Singleton.class);
        bind(ClusterService.class).to(ClusterServiceImpl.class);
        bindListener(Matchers.any(), new ClusteredListener(getProvider(ClusteredProviders.class), getProvider(ClusterService.class)));
    }

}
