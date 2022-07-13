package ru.finex.ws.network;

import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.service.NettyNetworkService;
import ru.finex.network.netty.service.impl.NettyNetworkServiceImpl;

import java.net.InetSocketAddress;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class NetworkServiceProvider implements Provider<NettyNetworkService> {

    private final EventLoopGroupService eventLoopGroupService;
    private final AbstractNetworkPipeline pipeline;
    private final InetSocketAddress address;

    @Inject
    public NetworkServiceProvider(
        @Named("ClientNetwork") EventLoopGroupService eventLoopGroupService,
        @Named("ClientNetwork") AbstractNetworkPipeline pipeline,
        @Named("ClientNetwork") InetSocketAddress address) {
        this.eventLoopGroupService = eventLoopGroupService;
        this.pipeline = pipeline;
        this.address = address;
    }

    @Override
    public NettyNetworkService get() {
        return new NettyNetworkServiceImpl(eventLoopGroupService.getParentGroup(), eventLoopGroupService.getChildGroup(), pipeline, address);
    }
}
