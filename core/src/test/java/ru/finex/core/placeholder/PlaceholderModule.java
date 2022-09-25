package ru.finex.core.placeholder;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;

/**
 * @author m0nster.mind
 */
public class PlaceholderModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PlaceholderJuelModule());
    }

}
