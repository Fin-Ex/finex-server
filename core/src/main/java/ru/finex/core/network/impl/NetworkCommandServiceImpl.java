package ru.finex.core.network.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.GlobalContext;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.network.NetworkCommandContext;
import ru.finex.core.command.network.NetworkCommandScope;
import ru.finex.core.network.NetworkCommandService;
import ru.finex.core.network.PacketMetadata;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkCommandServiceImpl implements NetworkCommandService {

    private final NetworkCommandScope commandScope;

    @Override
    public List<Pair<AbstractNetworkCommand, NetworkCommandContext>> createCommands(PacketMetadata<PacketDeserializer<?>> metadata, NetworkDto dto,
        ClientSession session) {
        NetworkCommandContext ctx = createCommandContext(metadata, dto, session);

        Class[] commandTypes = ctx.getMetadata().getCommands();
        if (commandTypes.length == 0) {
            return Collections.emptyList();
        }

        List<Pair<AbstractNetworkCommand, NetworkCommandContext>> commands = new ArrayList<>(commandTypes.length);

        // enter to scope to create correct command
        commandScope.enterScope(ctx);
        for (int i = 0; i < commandTypes.length; i++) {
            Class<?> commandType = commandTypes[i];
            commands.add(
                Pair.of(
                    (AbstractNetworkCommand) GlobalContext.injector.getInstance(commandType),
                    ctx.newCommand()
                )
            );
        }
        commandScope.exitScope();

        return commands;
    }

    @Override
    public NetworkCommandContext createCommandContext(PacketMetadata<PacketDeserializer<?>> metadata, NetworkDto dto, ClientSession session) {
        return NetworkCommandContext.builder()
            .metadata(metadata)
            .dto(dto)
            .session(session)
            .build();
    }

}
