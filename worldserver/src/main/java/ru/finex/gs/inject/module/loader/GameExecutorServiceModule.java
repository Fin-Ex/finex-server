package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.finex.core.inject.LoaderModule;
import ru.finex.gs.concurrent.game.GameExecutorProvider;
import ru.finex.gs.concurrent.game.GameExecutorServiceImpl;
import ru.finex.gs.concurrent.game.GameTickServiceImpl;
import ru.finex.gs.service.concurrent.GameExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class GameExecutorServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(ExecutorService.class).annotatedWith(Names.named("Game")).toProvider(GameExecutorProvider.class);
        bind(GameExecutorService.class).to(GameExecutorServiceImpl.class);
        bind(GameTickServiceImpl.class).asEagerSingleton();
    }

}
