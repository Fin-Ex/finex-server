package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.uid.RuntimeIdService;
import ru.finex.core.uid.impl.ClusteredRuntimeIdService;

/**
 * @author m0nster.mind
 */
public class ClusteredUidModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RuntimeIdService.class).to(ClusteredRuntimeIdService.class);
    }

}
