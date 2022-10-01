package ru.finex.core.network.codec;

import io.netty.buffer.ByteBuf;
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

import java.util.List;
import javax.inject.Inject;

/**
 * Decodes {@link ByteBuf ByteBuf} into {@link Pair pair} of
 * {@link PacketMetadata packet metadata} and {@link NetworkDto network DTO}.
 *
 * Doesn't support singleton scope!
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
            log.warn("PacketMetadata not found for opcodes: {}", toStringOpcodes(opcodes));
            in.readerIndex(in.writerIndex());
            return;
        }

        PacketDeserializer<?> serial = metadata.getSerial();
        NetworkDto dto = null;
        if (serial != null) {
            dto = serial.deserialize(in);
        }

        if (in.isReadable()) {
            log.debug("Serializer '{}' is not fully read payload!", metadata.getSerial().getClass().getCanonicalName());
            in.readerIndex(in.writerIndex());
        }

        out.add(Pair.of(metadata, dto));
    }

    private static String toStringOpcodes(int[] opcodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < opcodes.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }

            String hex = Integer.toHexString(opcodes[i]);
            sb.append("0x");
            if (hex.length() % 2 != 0) {
                sb.append("0");
            }
            sb.append(hex);
        }

        return sb.append("]")
            .toString();
    }
}
