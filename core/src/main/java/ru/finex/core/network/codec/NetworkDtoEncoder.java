package ru.finex.core.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.network.PacketMetadata;
import ru.finex.core.network.PacketService;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.serial.PacketSerializer;

import javax.inject.Inject;

/**
 * Encode {@link NetworkDto network dto} into {@link ByteBuf byte buffer}.
 * @author m0nster.mind
 */
@Slf4j
@Sharable
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkDtoEncoder extends MessageToByteEncoder<NetworkDto> {

    private final PacketService packetService;
    private final OpcodeCodec opcodeCodec;

    @Override
    protected void encode(ChannelHandlerContext ctx, NetworkDto msg, ByteBuf out) throws Exception {
        Class<? extends NetworkDto> type = msg.getClass();
        PacketMetadata<PacketSerializer<?>> metadata = packetService.getOutcomePacketMetadata(type);
        if (metadata == null) {
            log.warn("PacketMetadata not found for DTO: {}", type.getCanonicalName());
            return;
        }

        opcodeCodec.encode(metadata.getOpcodes(), out);
        PacketSerializer serial = metadata.getSerial();
        try {
            serial.serialize(msg, out);
        } catch (Exception e) {
            // log and throw exception because netty is hide it
            log.error("Fail to serialize message: '{}' / {}", msg, msg.getClass().getCanonicalName(), e);
            throw new EncoderException(e);
        }
    }

}
