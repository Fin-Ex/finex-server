package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.concurrent.game.GameExecutorProvider;
import ru.finex.ws.concurrent.game.GameExecutorServiceImpl;
import ru.finex.ws.service.concurrent.GameExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode
public class GameExecutorServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(ExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(GameExecutorService.class).to(GameExecutorServiceImpl.class);
    }

}
