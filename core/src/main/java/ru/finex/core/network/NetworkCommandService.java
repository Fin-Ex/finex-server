package ru.finex.core.network;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.network.netty.model.ClientSession;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface NetworkCommandService {

    /**
     * Создает список команд связанных с входящим пакетом.
     * @param opcodes опкоды пакета
     * @param buffer данные пакета
     * @param session сетевая сессия с игровым клиентом
     * @return список команд для выполнения с контекстом
     */
    List<Pair<AbstractNetworkCommand, NetworkCommandContext>> createCommands(int[] opcodes, ByteBuf buffer, ClientSession session);

    /**
     * Создает контекст для команд исходя из полученного пакета.
     * @param opcodes опкоды пакета
     * @param buffer данные пакета
     * @param session сетевая сессия с игровым клиентом
     * @return контекст для команд
     */
    NetworkCommandContext createCommandContext(int[] opcodes, ByteBuf buffer, ClientSession session);

}
