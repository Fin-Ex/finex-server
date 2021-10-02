package ru.finex.ws.inject.module.loader;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.command.InputCommandService;
import ru.finex.ws.command.impl.InputCommandServiceImpl;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(InputCommandService.class).to(InputCommandServiceImpl.class);
    }
}
