package ru.finex.core.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.Objects;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
public class ConfigProvider implements Provider<Config> {

    private final Config config;

    public ConfigProvider(String configName) {
        config = loadConfig(configName);
        Objects.requireNonNull(config, "Not found configuration!");
    }

    @Override
    public Config get() {
        return config;
    }

    private Config loadConfig(String configName) {
        Config config = tryLoadFromFilesystem(configName);
        if (config == null) {
            config = tryLoadFromClasspath(configName);
        }

        return config == null ? ConfigFactory.empty() : config;
    }

    private Config tryLoadFromFilesystem(String configName) {
        try {
            File file = new File("resources", configName);
            if(!file.exists()) {
                return null;
            }
            return ConfigFactory.parseFile(file);
        } catch (Exception e) {
            return null;
        }
    }

    private Config tryLoadFromClasspath(String configName) {
        try {
            return ConfigFactory.load(configName);
        } catch (Exception e) {
            return null;
        }
    }

}
