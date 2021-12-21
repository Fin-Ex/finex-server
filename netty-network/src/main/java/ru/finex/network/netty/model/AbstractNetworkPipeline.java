package ru.finex.network.netty.model;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author m0nster.mind
 */
public abstract class AbstractNetworkPipeline extends ChannelInitializer<SocketChannel> {

    @Override
    protected abstract void initChannel(SocketChannel ch) throws Exception;

}
