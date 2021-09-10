package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.cluster.ServerRole;
import ru.finex.core.cluster.impl.ClusterServiceImpl;
import ru.finex.core.cluster.impl.ConfigProvider;
import ru.finex.core.cluster.impl.HazelcastProvider;
import ru.finex.core.inject.LoaderModule;
import ru.finex.gs.cluster.ServerRoleProvider;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class ClusterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerRole.class).toProvider(ServerRoleProvider.class);
        bind(Config.class).toProvider(ConfigProvider.class);
        bind(HazelcastInstance.class).toProvider(HazelcastProvider.class);
        bind(ClusterService.class).to(ClusterServiceImpl.class);
    }
    
}
