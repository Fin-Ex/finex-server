package ru.finex.core.hocon;

import com.typesafe.config.Config;
import ru.finex.core.GlobalContext;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ApplicationConfigProvider implements Provider<Config> {

    public static final String CONFIG_ARG = "config";

    private final ConfigProvider provider;

    public ApplicationConfigProvider() {
        provider = new ConfigProvider(GlobalContext.arguments.getOrDefault("config", "application.conf"));
    }

    @Override
    public Config get() {
        return provider.get();
    }

}
