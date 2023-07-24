package ru.finex.relay.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import lombok.EqualsAndHashCode;
import ru.finex.core.GlobalContext;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.command.network.NetworkCommandScope;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.ManagementModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.core.network.NetworkCommandService;
import ru.finex.core.network.PacketListener;
import ru.finex.core.network.PacketService;
import ru.finex.core.network.impl.NetworkCommandServiceImpl;
import ru.finex.core.network.impl.PacketServiceImpl;
import ru.finex.core.tick.TickService;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.relay.command.CommandExecutorProvider;
import ru.finex.relay.command.RelayCommandQueue;
import ru.finex.relay.service.ClientSessionService;
import ru.finex.relay.service.TopicResolverService;
import ru.finex.relay.service.impl.ClientSessionServiceImpl;
import ru.finex.relay.service.impl.TickServiceImpl;
import ru.finex.relay.service.impl.TopicResolverServiceImpl;

import java.util.concurrent.Executor;

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
        installNetworkModule();
        install(new ManagementModule());

        bind(TickService.class).to(TickServiceImpl.class);
        bind(ClientSessionService.class).to(ClientSessionServiceImpl.class);
        bind(TopicResolverService.class).to(TopicResolverServiceImpl.class);
    }

    private void installNetworkModule() {
        bind(PacketService.class).to(PacketServiceImpl.class);
        bind(NetworkCommandService.class).to(NetworkCommandServiceImpl.class);

        var commandScope = new NetworkCommandScope();
        bindScope(NetworkCommandScoped.class, commandScope);
        bind(NetworkCommandScope.class).toInstance(commandScope);

        bind(Executor.class).toProvider(CommandExecutorProvider.class).in(Scopes.SINGLETON);
        bind(NetworkCommandQueue.class).to(RelayCommandQueue.class);
        bindListener(Matchers.any(), new PacketListener());
        GlobalContext.reflections.getSubTypesOf(PacketDeserializer.class)
            .forEach(this::bind);
        GlobalContext.reflections.getSubTypesOf(PacketSerializer.class)
            .forEach(this::bind);
    }

}
