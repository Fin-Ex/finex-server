package ru.finex.core.network;

import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.network.NetworkCommandContext;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface NetworkCommandService {

    /**
     * Создает список команд связанных с входящим пакетом.
     * Внимание: сессия в контексте не устанавливается!
     * @param metadata мета-данные пакета
     * @param dto данные пакета
     * @param session сессия клиента
     * @return список команд для выполнения с контекстом
     */
    List<Pair<AbstractNetworkCommand, NetworkCommandContext>> createCommands(PacketMetadata<PacketDeserializer<?>> metadata, NetworkDto dto,
        ClientSession session);

    /**
     * Создает контекст для команд исходя из полученного пакета.
     * Внимание: сессия в контексте не устанавливается!
     * @param metadata мета-данные пакета
     * @param dto данные пакета
     * @param session сессия клиента
     * @return контекст для команд
     */
    NetworkCommandContext createCommandContext(PacketMetadata<PacketDeserializer<?>> metadata, NetworkDto dto, ClientSession session);

}
