package ru.finex.auth.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.auth.AuthServerApplication;
import ru.finex.core.ServerContext;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.PoolModule;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class AuthServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServerContext.class).to(AuthServerApplication.class);
        install(new DbModule());
        install(new HoconModule());
        install(new ClusterModule());
        install(new PoolModule());
    }

}
