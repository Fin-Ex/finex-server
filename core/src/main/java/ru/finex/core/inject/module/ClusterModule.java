package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.impl.ClusterServiceImpl;
import ru.finex.core.cluster.impl.ConfigProvider;
import ru.finex.core.cluster.impl.HazelcastProvider;

/**
 * @author m0nster.mind
 */
public class ClusterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).toProvider(ConfigProvider.class);
        bind(HazelcastInstance.class).toProvider(HazelcastProvider.class);
        bind(ClusterService.class).to(ClusterServiceImpl.class);
    }

}
