package ru.finex.network.netty.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.network.netty.model.NetworkDto;

/**
 * Packet serializer.
 * Serialize (transform object into bytes) data in object representation into network packet payload.
 * @param <T> specified data type what serializer consumes
 * @author m0nster.mind
 */
public interface PacketSerializer<T extends NetworkDto> {

    /**
     * Serialize object into network packet payload.
     * @param dto data in object representation
     * @return packet payload
     */
    ByteBuf serialize(T dto);

}
