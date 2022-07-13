package ru.finex.ws.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Opcode;
import ru.finex.core.network.OutcomePacket;
import ru.finex.network.netty.serial.PacketSerializer;
import ru.finex.ws.model.dto.OutputTestDto;

import java.nio.charset.StandardCharsets;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@OutcomePacket(@Opcode(0x02))
public class TestSerializer implements PacketSerializer<OutputTestDto> {

    @Override
    public void serialize(OutputTestDto dto, ByteBuf buffer) {
        byte[] serverVersion = dto.getServerVersion().getBytes(StandardCharsets.UTF_8);
        buffer.writeIntLE(serverVersion.length);
        buffer.writeBytes(serverVersion);
    }

}
