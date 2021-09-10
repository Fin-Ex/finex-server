package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerContext;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.WorldServerApplication;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class GameServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerContext.class).to(WorldServerApplication.class);
        bind(ApplicationBuilt.class).to(WorldServerApplication.class);
    }

}
