package ru.finex.core.cluster.impl;

import com.hazelcast.config.ClasspathYamlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemYamlConfig;
import com.hazelcast.config.MemberAttributeConfig;
import ru.finex.core.cluster.ServerRole;

import java.io.File;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ConfigProvider implements Provider<Config> {

    private final Config config;

    @Inject
    public ConfigProvider(ServerRole role) {
        config = loadConfig("hazelcast.yml", role);
    }

    @Override
    public Config get() {
        return config;
    }

    private Config loadConfig(String configName, ServerRole serverRole) {
        Config config = tryLoadFromFilesystem(configName);
        if (config == null) {
            config = tryLoadFromClasspath(configName);
        }

        Objects.requireNonNull(config, "Hazelcast configuration [hazelcast.yml] not found!");
        MemberAttributeConfig memberConfig = config.getMemberAttributeConfig();
        memberConfig.setAttribute(ServerRole.ATTRIBUTE_NAME, serverRole.getRole());

        return config;
    }

    private Config tryLoadFromFilesystem(String configName) {
        try {
            File file = new File("resources", configName);
            if(!file.exists()) {
                return null;
            }

            return new FileSystemYamlConfig(file.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Config tryLoadFromClasspath(String configName) {
        try {
            return new ClasspathYamlConfig(configName);
        } catch (Exception e) {
            return null;
        }
    }

}
