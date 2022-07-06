package ru.finex.auth.inject.module;

import com.google.inject.AbstractModule;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.PoolModule;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode
public class AuthServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
        install(new ClusterModule());
        install(new PoolModule());
    }

}
