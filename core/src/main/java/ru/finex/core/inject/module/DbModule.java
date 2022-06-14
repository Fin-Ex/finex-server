package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import org.hibernate.Session;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.db.impl.DbSessionServiceImpl;
import ru.finex.core.db.impl.HibernatePropertyProvider;
import ru.finex.core.db.impl.HibernateSessionProvider;
import ru.finex.core.db.impl.ServiceRegistryProvider;
import ru.finex.core.db.impl.TransactionalMethodInterceptor;
import ru.finex.core.db.migration.impl.ClasspathScannerImpl;
import ru.finex.core.db.migration.impl.DataSourceProvider;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.DefaultCrudRepository;
import ru.finex.core.repository.RepositoryExecutorServiceProvider;
import ru.finex.core.repository.RepositoryProxy;
import ru.finex.evolution.ClasspathScanner;
import ru.finex.evolution.MigrationService;
import ru.finex.evolution.impl.MigrationServiceImpl;

import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import javax.transaction.Transactional;

/**
 * @author m0nster.mind
 */
public class DbModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Map<String, String>>() { }).annotatedWith(Names.named("HibernateProperties"))
            .toProvider(HibernatePropertyProvider.class);
        bind(ServiceRegistry.class).toProvider(ServiceRegistryProvider.class);
        bind(DbSessionService.class).to(DbSessionServiceImpl.class);
        bind(Session.class).toProvider(HibernateSessionProvider.class);
        bind(DataSource.class).annotatedWith(Names.named("Migration")).toProvider(DataSourceProvider.class);
        bind(ClasspathScanner.class).to(ClasspathScannerImpl.class);
        bind(MigrationService.class).to(MigrationServiceImpl.class);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), new TransactionalMethodInterceptor());
        bind(ExecutorService.class).annotatedWith(Names.named("RepositoryExecutor")).toProvider(RepositoryExecutorServiceProvider.class);
        bindCrudRepositories();
    }

    protected void bindCrudRepositories() {
        // provide proxy implementation for all crud interfaces
        GlobalContext.reflections.getSubTypesOf(CrudRepository.class)
            .stream()
            .filter(DbModule::checkRepositoryModifiers)
            .filter(e -> e != CrudRepository.class) // TODO m0nster.mind: ignore all repositories that doesnt have end-type of generic
            .map(interfaceType -> new RepositoryProxy(defaultCrudRepository(interfaceType), interfaceType))
            .forEach(proxyHandler -> bind(TypeLiteral.get(proxyHandler.getInterfaceType())).toInstance(repositoryProxy(proxyHandler)));
    }

    private static Object repositoryProxy(RepositoryProxy proxyHandler) {
        return Proxy.newProxyInstance(
            DbModule.class.getClassLoader(),
            new Class[] {proxyHandler.getInterfaceType()},
            proxyHandler
        );
    }

    private DefaultCrudRepository defaultCrudRepository(Class<? extends CrudRepository> interfaceType) {
        var repository = DefaultCrudRepository.builder()
            .entityTypeFromInterface(interfaceType)
            .build();

        requestInjection(repository);
        return repository;
    }

    private static boolean checkRepositoryModifiers(Class<?> type) {
        int modifiers = type.getModifiers();
        return Modifier.isInterface(modifiers) && Modifier.isPublic(modifiers);
    }

}
