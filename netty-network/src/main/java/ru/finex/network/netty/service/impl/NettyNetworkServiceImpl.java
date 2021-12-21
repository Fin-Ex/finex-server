package ru.finex.network.netty.service.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.finex.network.netty.model.AbstractNetworkPipeline;
import ru.finex.network.netty.service.NettyNetworkService;

import java.net.InetSocketAddress;
import java.util.Objects;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class NettyNetworkServiceImpl implements NettyNetworkService {

    private final ServerBootstrap bootstrap;
    private final InetSocketAddress address;
    private volatile ChannelFuture channelFuture;

    @Inject
    public NettyNetworkServiceImpl(EventLoopGroup parentGroup, EventLoopGroup childGroup, AbstractNetworkPipeline networkPipeline,
        InetSocketAddress address) {
        this.address = address;
        bootstrap = new ServerBootstrap()
            .group(parentGroup, childGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(networkPipeline);
    }

    @Override
    public void bind() {
        if (channelFuture != null) {
            throw new RuntimeException(String.format(
                "Network already bind to %s",
                channelFuture.channel().localAddress().toString()
            ));
        }
        channelFuture = bootstrap.bind(address);
    }

    @Override
    public void shutdown(Runnable callback) {
        Objects.requireNonNull(channelFuture);
        channelFuture.channel().close().addListener(e -> callback.run());
    }

    @Override
    public void shutdown() {
        Objects.requireNonNull(channelFuture);
        channelFuture.channel().close();
    }

    @Override
    public void shutdownNow() throws InterruptedException {
        Objects.requireNonNull(channelFuture);
        channelFuture.channel().close().sync();
    }

}
