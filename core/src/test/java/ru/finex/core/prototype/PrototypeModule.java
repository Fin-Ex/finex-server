package ru.finex.core.prototype;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;

/**
 * @author m0nster.mind
 */
public class PrototypeModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
    }

}
