package ru.finex.ws.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.EqualsAndHashCode;
import ru.finex.core.inject.LoaderModule;
import ru.finex.ws.concurrent.service.ServiceExecutorProvider;
import ru.finex.ws.concurrent.service.ServiceExecutorServiceImpl;
import ru.finex.ws.service.concurrent.ServiceExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode
public class ServiceExecutorServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).annotatedWith(Names.named("Service")).toProvider(ServiceExecutorProvider.class);
        bind(ExecutorService.class).annotatedWith(Names.named("Service")).toProvider(ServiceExecutorProvider.class);
        bind(ServiceExecutorService.class).to(ServiceExecutorServiceImpl.class);
    }

}
