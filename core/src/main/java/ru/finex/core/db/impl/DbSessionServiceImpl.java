package ru.finex.core.db.impl;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.model.entity.Entity;
import ru.finex.evolution.MigrationService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class DbSessionServiceImpl implements DbSessionService {

    private static final String EVO_AUTO_ROLLBACK = "--evo-auto-rollback";

    @Getter
    private final SessionFactory sessionFactory;

    @Inject
    public DbSessionServiceImpl(ServiceRegistry serviceRegistry, MigrationService migrationService) {
        // do migration before up hibernate
        migrationService.autoMigration(GlobalContext.arguments.containsKey(EVO_AUTO_ROLLBACK));

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
