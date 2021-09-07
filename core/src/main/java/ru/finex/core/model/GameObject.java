package ru.finex.core.model;

import com.google.inject.Injector;
import ru.finex.core.events.EventBus;

/**
 * @author m0nster.mind
 */
public interface GameObject {

    int getRuntimeId();
    int getPersistenceId();

    EventBus getEventBus();
    Injector getInjector();

}
