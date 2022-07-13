package ru.finex.ws.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import ru.finex.core.events.EventBus;
import ru.finex.core.events.local.LocalEventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.service.NettyNetworkService;
import ru.finex.ws.model.event.GameSessionEvent;
import ru.finex.ws.network.EventLoopGroupService;
import ru.finex.ws.network.NetworkAddressProvider;
import ru.finex.ws.network.NetworkPipeline;
import ru.finex.ws.network.NetworkServiceProvider;
import ru.finex.ws.network.OpcodeCodecImpl;

import java.net.InetSocketAddress;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class ClientNetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventLoopGroupService.class).annotatedWith(Names.named("ClientNetwork")).to(EventLoopGroupService.class).in(Scopes.SINGLETON);
        bind(AbstractNetworkPipeline.class).annotatedWith(Names.named("ClientNetwork")).to(NetworkPipeline.class).in(Scopes.SINGLETON);
        bind(InetSocketAddress.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkAddressProvider.class).in(Scopes.SINGLETON);
        bind(NettyNetworkService.class).annotatedWith(Names.named("ClientNetwork")).toProvider(NetworkServiceProvider.class).in(Scopes.SINGLETON);
        bind(OpcodeCodec.class).to(OpcodeCodecImpl.class);
        bind(new TypeLiteral<EventBus<GameSessionEvent>>() { }).toInstance(new LocalEventBus<>());
    }

}
