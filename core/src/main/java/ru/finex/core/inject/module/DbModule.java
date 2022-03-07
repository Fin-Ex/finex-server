package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import org.hibernate.Session;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.db.impl.DbSessionServiceImpl;
import ru.finex.core.db.impl.HibernateConfigProvider;
import ru.finex.core.db.impl.HibernateSessionProvider;
import ru.finex.core.db.impl.ServiceRegistryProvider;
import ru.finex.core.db.impl.TransactionalMethodInterceptor;
import ru.finex.core.db.migration.impl.ClasspathScannerImpl;
import ru.finex.core.db.migration.impl.DataSourceProvider;
import ru.finex.evolution.ClasspathScanner;
import ru.finex.evolution.MigrationService;
import ru.finex.evolution.impl.MigrationServiceImpl;

import java.net.URL;
import javax.sql.DataSource;
import javax.transaction.Transactional;

/**
 * @author m0nster.mind
 */
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(URL.class).annotatedWith(Names.named("HibernateConfig")).toProvider(HibernateConfigProvider.class);
        bind(ServiceRegistry.class).toProvider(ServiceRegistryProvider.class);
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);
        bind(DataSource.class).annotatedWith(Names.named("Migration")).toProvider(DataSourceProvider.class);
        bind(ClasspathScanner.class).to(ClasspathScannerImpl.class);
        bind(MigrationService.class).to(MigrationServiceImpl.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), new TransactionalMethodInterceptor());
    }

}
