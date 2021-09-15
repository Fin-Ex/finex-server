package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.hibernate.Session;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionServiceImpl;
import ru.finex.core.db.HibernateConfigProvider;
import ru.finex.core.db.HibernateSessionProvider;
import ru.finex.core.db.ServiceRegistryProvider;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.service.DbSessionService;
import ru.finex.core.service.MigrationService;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(URL.class).annotatedWith(Names.named("HibernateConfig")).toProvider(HibernateConfigProvider.class);
        bind(ServiceRegistry.class).toProvider(ServiceRegistryProvider.class);
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);

        List<Provider<MigrationService>> migrationProviders = GlobalContext.reflections.getSubTypesOf(MigrationService.class)
            .stream()
            .filter(e -> !Modifier.isAbstract(e.getModifiers()) && !e.isInterface())
            .map(e -> (Provider<MigrationService>) getProvider(e))
            .collect(Collectors.toList());
        bind(new TypeLiteral<List<Provider<MigrationService>>>() { }).toInstance(migrationProviders);
    }

}
