package ru.finex.core.db.impl;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    private final ServiceRegistry serviceRegistry;

    @Inject
    public ServiceRegistryProvider(@Named("HibernateProperties") Map<String, String> hibernateProperties) {
        serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(hibernateProperties)
            .build();
    }

    @Override
    public ServiceRegistry get() {
        return serviceRegistry;
    }
}
