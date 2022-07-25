package ru.finex.core.db.impl;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Produces hibernate parameters from typesafe config.
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class HibernatePropertyProvider implements Provider<Map<String, Object>> {

    private final Config config;

    @Override
    public Map<String, Object> get() {
        return config.getConfig("hibernate").entrySet()
            .stream()
            .map(this::mapProperty)
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Pair<String, String> mapProperty(Map.Entry<String, ConfigValue> entry) {
        String key = entry.getKey();
        if (!key.startsWith("hibernate.")) {
            key = "hibernate." + key;
        }

        return Pair.of(key, entry.getValue().unwrapped().toString());
    }

}
