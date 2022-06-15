package ru.finex.core.command.network;

import lombok.Builder;
import lombok.Data;
import ru.finex.core.command.CommandContext;
import ru.finex.core.network.PacketMetadata;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Контекст для сетевой команды.
 * Включает в себя: мета-данные пакета, пэйлоад (dto), сессию клиента и переменные (ключ-значение).
 *
 * @author m0nster.mind
 */
@Data
@Builder
public class NetworkCommandContext implements CommandContext {

    private final Map<Object, Object> variables = new HashMap<>();
    private PacketMetadata<?> metadata;
    private NetworkDto dto;
    private ClientSession session;

    /**
     * Duplicate current context for command running.
     * Excludes only variables.
     * @return new context for command running
     */
    public NetworkCommandContext newCommand() {
        return new NetworkCommandContext(metadata, dto, session);
    }

}
