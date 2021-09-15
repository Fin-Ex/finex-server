package ru.finex.core.db;

import com.google.inject.Provider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.GlobalContext;
import ru.finex.core.model.entity.Entity;
import ru.finex.core.service.DbSessionService;
import ru.finex.core.service.MigrationService;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class DbSessionServiceImpl implements DbSessionService {

    private final SessionFactory sessionFactory;

    @Inject
    public DbSessionServiceImpl(ServiceRegistry serviceRegistry, List<Provider<MigrationService>> migrationServices) {
        migrationServices.stream()
            .map(Provider::get)
            .sorted()
            .forEach(migrationService -> {
                migrationService.migrateToLastVersion();
                migrationService.doneMigration();
            });

        MetadataSources metaSrc = new MetadataSources(serviceRegistry);
        GlobalContext.reflections.getSubTypesOf(Entity.class).forEach(metaSrc::addAnnotatedClass);

        Metadata metadata = metaSrc.getMetadataBuilder()
            .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
            .build();

        sessionFactory = metadata.buildSessionFactory();
    }

    @Override
    public Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
