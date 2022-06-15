package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import ru.finex.core.GlobalContext;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.command.network.NetworkCommandQueueImpl;
import ru.finex.core.command.network.NetworkCommandScope;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.core.network.NetworkCommandService;
import ru.finex.core.network.PacketListener;
import ru.finex.core.network.PacketService;
import ru.finex.core.network.impl.NetworkCommandServiceImpl;
import ru.finex.core.network.impl.PacketServiceImpl;
import ru.finex.network.netty.serial.PacketDeserializer;
import ru.finex.network.netty.serial.PacketSerializer;

/**
 * Core network module.
 * @author m0nster.mind
 */
public class NetworkModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PacketService.class).to(PacketServiceImpl.class);
        bind(NetworkCommandService.class).to(NetworkCommandServiceImpl.class);

        var commandScope = new NetworkCommandScope();
        bindScope(NetworkCommandScoped.class, commandScope);
        bind(NetworkCommandScope.class).toInstance(commandScope);

        bind(NetworkCommandQueue.class).to(NetworkCommandQueueImpl.class);
        bindListener(Matchers.any(), new PacketListener());
        GlobalContext.reflections.getSubTypesOf(PacketDeserializer.class)
            .forEach(this::bind);
        GlobalContext.reflections.getSubTypesOf(PacketSerializer.class)
            .forEach(this::bind);
    }

}
