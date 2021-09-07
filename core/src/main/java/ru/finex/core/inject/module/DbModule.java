package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.hibernate.Session;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.db.DbSessionServiceImpl;
import ru.finex.core.db.HibernateConfigProvider;
import ru.finex.core.db.HibernateSessionProvider;
import ru.finex.core.db.MigrationServiceImpl;
import ru.finex.core.db.ServiceRegistryProvider;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.service.DbSessionService;
import ru.finex.core.service.MigrationService;

import java.net.URL;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(URL.class).annotatedWith(Names.named("HibernateConfig")).toProvider(HibernateConfigProvider.class);
        bind(ServiceRegistry.class).toProvider(ServiceRegistryProvider.class);
        bind(MigrationService.class).to(MigrationServiceImpl.class);
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);
    }

}
