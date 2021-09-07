package ru.finex.gs.concurrent.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ServiceExecutorProvider implements Provider<ScheduledExecutorService> {

    private final ScheduledThreadPoolExecutor executorService;

    @Inject
    public ServiceExecutorProvider(ServiceExecutorConfiguration conf) {
        executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
            conf.getMinimalThreads(),
            new ThreadFactoryBuilder()
                .setNameFormat("GameThread-%d")
                .build()
        );

        executorService.setKeepAliveTime(conf.getKeepAlive(), TimeUnit.MILLISECONDS);
        executorService.setMaximumPoolSize(conf.getMaximalThreads());
        executorService.prestartCoreThread();
    }

    @Override
    public ScheduledExecutorService get() {
        return executorService;
    }
}
