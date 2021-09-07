package ru.finex.core.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.util.Objects;

/**
 * @author m0nster.mind
 */
@Singleton
public class ApplicationConfigProvider implements Provider<Config> {

    private final ConfigProvider provider;

    public ApplicationConfigProvider() {
        provider = new ConfigProvider("application.conf");
    }

    @Override
    public Config get() {
        return provider.get();
    }

}
