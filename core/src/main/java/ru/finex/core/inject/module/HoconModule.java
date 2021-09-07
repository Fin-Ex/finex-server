package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.typesafe.config.Config;
import ru.finex.core.hocon.ConfigListener;
import ru.finex.core.hocon.ApplicationConfigProvider;
import ru.finex.core.inject.LoaderModule;

/**
 * Модуль для HOCON-конфигурации.
 * Добавляет в контекст {@link Config}, а также обработку аннотации {@link ru.finex.core.hocon.ConfigResource}.
 *
 * @author m0nster.mind
 */
@LoaderModule
public class HoconModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).toProvider(ApplicationConfigProvider.class);
        bindListener(Matchers.any(), new ConfigListener());
    }

}
