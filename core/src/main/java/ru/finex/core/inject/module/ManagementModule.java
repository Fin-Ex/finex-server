package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.management.HawtioService;
import ru.finex.core.management.HawtioServiceImpl;

/**
 * @author m0nster.mind
 */
public class ManagementModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HawtioService.class).to(HawtioServiceImpl.class);
    }

}
