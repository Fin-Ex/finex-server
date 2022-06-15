package ru.finex.core.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.network.PacketMetadata;
import ru.finex.core.network.PacketService;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.OpcodeCodec;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * Decodes {@link ByteBuf ByteBuf} into {@link Pair pair} of
 * {@link PacketMetadata packet metadata} and {@link NetworkDto network DTO}.
 *
 * Doesnt support singleton scope!
 *
 * @author m0nster.mind
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkDtoDecoder extends ByteToMessageDecoder {

    private final OpcodeCodec opcodeCodec;
    private final PacketService packetService;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int[] opcodes = opcodeCodec.decode(in);
        PacketMetadata<PacketDeserializer<?>> metadata = packetService.getIncomePacketMetadata(opcodes);
        if (metadata == null) {
            log.warn("PacketMetadata not found for opcodes: {}", Arrays.toString(opcodes));
            return;
        }

        PacketDeserializer<?> serial = metadata.getSerial();
        NetworkDto dto = null;
        if (serial != null) {
            dto = serial.deserialize(in);
        }

        out.add(Pair.of(metadata, dto));
    }
}
