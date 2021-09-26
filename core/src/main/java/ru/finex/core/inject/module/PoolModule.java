package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.pool.PoolService;
import ru.finex.core.pool.impl.PoolServiceImpl;

/**
 * @author m0nster.mind
 */
public class PoolModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PoolService.class).to(PoolServiceImpl.class);
    }

}
