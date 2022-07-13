package ru.finex.ws.network.serializers;

import io.netty.buffer.ByteBuf;
import ru.finex.core.network.Cmd;
import ru.finex.core.network.IncomePacket;
import ru.finex.core.network.Opcode;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.ws.commands.network.TestCommand;
import ru.finex.ws.model.dto.InputTestDto;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@IncomePacket(value = @Opcode(0x01), command = @Cmd(TestCommand.class))
public class TestDeserializer implements PacketDeserializer<InputTestDto> {

    @Override
    public InputTestDto deserialize(ByteBuf buffer) {
        return new InputTestDto(buffer.readIntLE());
    }

}
