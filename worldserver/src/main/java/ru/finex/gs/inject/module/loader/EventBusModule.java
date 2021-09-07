package ru.finex.gs.inject.module.loader;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.finex.core.events.EventBus;
import ru.finex.core.inject.LoaderModule;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class EventBusModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(Names.named("Global")).toInstance(new EventBus());
    }

}
