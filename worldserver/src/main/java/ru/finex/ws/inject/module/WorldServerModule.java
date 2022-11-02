package ru.finex.ws.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.GameObjectModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.ManagementModule;
import ru.finex.core.inject.module.NetworkModule;
import ru.finex.core.inject.module.PersistenceModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.impl.RegisterTickListener;
import ru.finex.ws.tick.impl.TickServiceImpl;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode(callSuper = false)
public class WorldServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
        install(new PlaceholderJuelModule());
        install(new ClusterModule());
        install(new ClusteredUidModule());
        install(new PersistenceModule());
        install(new PoolModule());
        install(new NetworkModule());
        install(new CommandModule());
        install(new ManagementModule());
        install(new GameObjectModule());
        bind(TickService.class).to(TickServiceImpl.class);
        bindListener(Matchers.any(), new RegisterTickListener());
    }

}
