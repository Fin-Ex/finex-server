package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.finex.core.ServerContext;
import ru.finex.core.cluster.ServerRole;
import ru.finex.core.events.EventBus;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ComponentModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.PersistenceModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.core.service.GameObjectPrototypeService;
import ru.finex.ws.WorldServerApplication;
import ru.finex.ws.cluster.ServerRoleProvider;
import ru.finex.ws.service.impl.GameObjectPrototypeServiceImpl;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class WorldServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(Names.named("Global")).toInstance(new EventBus());
        bind(ServerContext.class).to(WorldServerApplication.class);
        install(new DbModule());
        install(new HoconModule());
        bind(ServerRole.class).toProvider(ServerRoleProvider.class);
        install(new ClusterModule());
        install(new ComponentModule());
        install(new PersistenceModule());
        install(new PoolModule());
        bind(GameObjectPrototypeService.class).to(GameObjectPrototypeServiceImpl.class);
    }

}
