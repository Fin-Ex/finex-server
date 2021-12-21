package ru.finex.network.netty.serial;

import io.netty.buffer.ByteBuf;
import ru.finex.network.netty.model.NetworkDto;

/**
 * Packet deserializer.
 * Deserialize network packet payload into object representation of data.
 *
 * @param <T> specified data type what deserializer produces
 * @author m0nster.mind
 */
public interface PacketDeserializer<T extends NetworkDto> {

    /**
     * Deserialize network packet payload to data.
     * @param buffer payload
     * @return data object
     */
    T deserialize(ByteBuf buffer);

}
