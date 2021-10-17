package ru.finex.core.hocon;

import com.typesafe.config.Config;

import javax.inject.Provider;
import javax.inject.Singleton;

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
