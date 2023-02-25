package ru.finex.relay.inject.module;

import com.google.inject.AbstractModule;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.ManagementModule;
import ru.finex.core.inject.module.NetworkModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.core.tick.TickService;
import ru.finex.relay.service.ClientSessionService;
import ru.finex.relay.service.TopicResolverService;
import ru.finex.relay.service.impl.ClientSessionServiceImpl;
import ru.finex.relay.service.impl.TickServiceImpl;
import ru.finex.relay.service.impl.TopicResolverServiceImpl;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode(callSuper = false)
public class RelayServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new HoconModule());
        install(new PlaceholderJuelModule());
        install(new ClusterModule());
        install(new ClusteredUidModule());
        install(new PoolModule());
        install(new NetworkModule());
        install(new ManagementModule());

        bind(TickService.class).to(TickServiceImpl.class);
        bind(ClientSessionService.class).to(ClientSessionServiceImpl.class);
        bind(TopicResolverService.class).to(TopicResolverServiceImpl.class);
    }

}
