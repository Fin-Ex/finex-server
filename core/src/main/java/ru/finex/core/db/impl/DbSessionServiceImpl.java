package ru.finex.core.db.impl;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.spi.XmlMappingBinderAccess;
import org.hibernate.service.ServiceRegistry;
import org.reflections.scanners.Scanners;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.evolution.MigrationService;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class DbSessionServiceImpl implements DbSessionService {

    private static final Pattern MAPPING_FILE = Pattern.compile("^mappings/[\\w\\d_-]+.xml$");
    private static final String EVO_AUTO_ROLLBACK = "--evo-auto-rollback";

    @Getter
    private final SessionFactory sessionFactory;

    @Inject
    public DbSessionServiceImpl(ServiceRegistry serviceRegistry, MigrationService migrationService,
        SnakeCasePhysicalNamingStrategy physicalNamingStrategy) {
        // do migration before up hibernate
        migrationService.autoMigration(GlobalContext.arguments.containsKey(EVO_AUTO_ROLLBACK));

        MetadataSources metaSrc = new MetadataSources(serviceRegistry);
        GlobalContext.reflections.getSubTypesOf(EntityObject.class).forEach(metaSrc::addAnnotatedClass);

        // load xml queries and mappings
        XmlMappingBinderAccess binder = metaSrc.getXmlMappingBinderAccess();
        Set<String> mappings = GlobalContext.reflections.get(ctx -> ctx.get(Scanners.Resources.index()).values()
                .stream()
                .flatMap(Collection::stream)
                .filter(e -> MAPPING_FILE.matcher(e).matches())
                .collect(Collectors.toSet())
        );
        mappings.stream()
            .map(e -> getClass().getClassLoader().getResource(e))
            .map(binder::bind)
            .forEach(metaSrc::addXmlBinding);

        Metadata metadata = metaSrc.getMetadataBuilder()
            .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
            .applyPhysicalNamingStrategy(physicalNamingStrategy)
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
