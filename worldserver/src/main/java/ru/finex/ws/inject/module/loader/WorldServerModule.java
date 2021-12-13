package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import ru.finex.core.component.ComponentService;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.PersistenceModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.ws.service.impl.ComponentServiceImpl;
import ru.finex.ws.tick.impl.RegisterTickListener;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class WorldServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
        install(new ClusterModule());
        install(new PersistenceModule());
        install(new PoolModule());
        bind(ComponentService.class).to(ComponentServiceImpl.class);
        bindListener(Matchers.any(), new RegisterTickListener());
    }

}
