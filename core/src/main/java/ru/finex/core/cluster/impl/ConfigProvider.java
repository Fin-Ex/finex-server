package ru.finex.core.cluster.impl;

import com.hazelcast.config.ClasspathYamlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemYamlConfig;
import com.hazelcast.config.MemberAttributeConfig;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.cluster.ServerRole;

import java.io.File;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
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
        Properties properties = buildProperties();
        Config config = tryLoadFromFilesystem(configName, properties);
        if (config == null) {
            log.warn("Hazelcast configuration [hazelcast.yml] not found in [resource/hazelcast.yml], use default configuration.");
            config = tryLoadFromClasspath(configName, properties);
        }

        MemberAttributeConfig memberConfig = config.getMemberAttributeConfig();
        memberConfig.setAttribute(ServerRole.ATTRIBUTE_NAME, serverRole.getRole());

        return config;
    }

    private Properties buildProperties() {
        Properties properties = new Properties(System.getProperties());
        properties.putAll(System.getenv());
        return properties;
    }

    private Config tryLoadFromFilesystem(String configName, Properties properties) {
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

    private Config tryLoadFromClasspath(String configName, Properties properties) {
        try {
            return new ClasspathYamlConfig(configName, properties);
        } catch (Exception e) {
            throw new RuntimeException("Hazelcast configuration [hazelcast.yml] not found!", e);
        }
    }

}
