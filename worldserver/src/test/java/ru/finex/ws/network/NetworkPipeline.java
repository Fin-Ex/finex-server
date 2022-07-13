package ru.finex.ws.network;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import ru.finex.core.network.codec.NetworkDtoDecoder;
import ru.finex.core.network.codec.NetworkDtoEncoder;
import ru.finex.network.netty.model.AbstractNetworkPipeline;

import java.nio.ByteOrder;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkPipeline extends AbstractNetworkPipeline {

    private static final int MAX_PACKET_SIZE = 8*1024;

    // Packet header include:
    //  - packet length
    private static final int PACKET_HEADER_LENGTH = 2;

    private final NetworkDtoEncoder encoder;
    private final Provider<NetworkDtoDecoder> decoderProvider;
    private final Provider<GameSession> gameSessionProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // outcome packets
        ch.pipeline().addLast(new LengthFieldBasedFrameEncoder(MAX_PACKET_SIZE, PACKET_HEADER_LENGTH));
        ch.pipeline().addLast(encoder);

        // income packets
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, MAX_PACKET_SIZE, 0, PACKET_HEADER_LENGTH, -PACKET_HEADER_LENGTH, PACKET_HEADER_LENGTH, false));
        ch.pipeline().addLast(decoderProvider.get()); // construct new decoder (netty restrictions, see ByteToMessageDecoder)
        ch.pipeline().addLast(gameSessionProvider.get());

    }

}
