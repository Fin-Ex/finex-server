package ru.finex.core.db;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import ru.finex.core.EnvConfigurator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.net.URL;

/**
 * @author m0nster.mind
 */
public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    private final ServiceRegistry serviceRegistry;

    @Inject
    public ServiceRegistryProvider(@Named("HibernateConfig") URL hibernateConfig, EnvConfigurator configurator) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
            .configure(hibernateConfig);
        configurator.configure(builder.getSettings());
        serviceRegistry = builder.build();
    }

    @Override
    public ServiceRegistry get() {
        return serviceRegistry;
    }
}
