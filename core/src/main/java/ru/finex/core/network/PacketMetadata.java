package ru.finex.core.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.network.netty.model.NetworkDto;

/**
 * @param <T> serializer/deserializer type
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
@NetworkCommandScoped
public class PacketMetadata<T> {

    private static final Class[] EMPTY_COMMANDS = new Class[0];

    private int[] opcodes;
    private Class[] commands;
    private Class<? extends NetworkDto> dataType;
    private T serial;

    public PacketMetadata(int[] opcodes, Class<? extends NetworkDto> dataType, T serial) {
        this(opcodes, EMPTY_COMMANDS, dataType, serial);
    }

    public int getLastOpcode() {
        return opcodes[opcodes.length - 1];
    }

}
