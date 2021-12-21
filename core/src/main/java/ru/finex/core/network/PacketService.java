package ru.finex.core.network;

import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

/**
 * @author m0nster.mind
 */
public interface PacketService {

    /**
     * Returns income packet metadata bound to specified opcodes.
     * @param opcodes opcodes
     * @return packet metadata or null if not found
     */
    PacketMetadata<PacketDeserializer<?>> getIncomePacketMetadata(int... opcodes);

    /**
     * Returns outcome packet metadata bound to specified opcodes.
     * @param opcodes opcodes
     * @return packet metadata or null if not found
     */
    PacketMetadata<PacketSerializer<?>> getOutcomePacketMetadata(int... opcodes);

    /**
     * Returns outcome packet metadata bound to specified data-type of serializer.
     * @param dataType data-type class
     * @param <T> data-type
     * @return packet metadata or null if not found
     */
    <T extends NetworkDto> PacketMetadata<PacketSerializer<?>> getOutcomePacketMetadata(Class<T> dataType);

}
