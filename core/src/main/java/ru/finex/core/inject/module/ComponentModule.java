package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.component.ComponentService;
import ru.finex.core.component.impl.ComponentServiceImpl;

/**
 * @author m0nster.mind
 */
public class ComponentModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ComponentService.class).to(ComponentServiceImpl.class);
    }

}
