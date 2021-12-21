package ru.finex.core.network;

import lombok.Builder;
import lombok.Data;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author m0nster.mind
 */
@Data
@Builder
public class NetworkCommandContext {

    private final Map<Object, Object> variables = new HashMap<>();
    private PacketMetadata<?> metadata;
    private PacketSerializer<?> serializer;
    private PacketDeserializer<?> deserializer;
    private NetworkDto dto;
    private ClientSession session;

    /**
     * Duplicate current context for command running.
     * Excludes only variables.
     * @return new context for command running
     */
    public NetworkCommandContext newCommand() {
        return new NetworkCommandContext(metadata, serializer, deserializer, dto, session);
    }

}
