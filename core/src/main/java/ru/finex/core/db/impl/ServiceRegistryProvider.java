package ru.finex.core.db.impl;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ServiceRegistryProvider implements Provider<ServiceRegistry> {

    private final ServiceRegistry serviceRegistry;

    @Inject
    public ServiceRegistryProvider(Config config) {
        Map<String, String> hibernateProp = config.getConfig("hibernate").entrySet()
            .stream()
            .map(this::mapProperty)
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(hibernateProp)
            .build();
    }

    private Pair<String, String> mapProperty(Map.Entry<String, ConfigValue> entry) {
        String key = entry.getKey();
        if (!key.startsWith("hibernate.")) {
            key = "hibernate." + key;
        }

        return Pair.of(key, entry.getValue().render());
    }

    @Override
    public ServiceRegistry get() {
        return serviceRegistry;
    }
}
