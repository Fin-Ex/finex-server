package ru.finex.core.db;

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

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class DbSessionServiceImpl implements DbSessionService {

    private final SessionFactory sessionFactory;

    @Inject
    public DbSessionServiceImpl(ServiceRegistry serviceRegistry, MigrationService migrationService) {
        migrationService.autoMigration(); // do migration before up hibernate

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
