package ru.finex.core.repository;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class RepositoryExecutorServiceProvider implements Provider<ExecutorService> {

    private final ExecutorService executorService;

    @Inject
    public RepositoryExecutorServiceProvider(RepositoryExecutorConfig config) {
        var executorService = new ThreadPoolExecutor(
            config.getMinThreads(),
            config.getMaxThreads(),
            config.getKeepAliveSecs(),
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            threadFactory()
        );
        executorService.prestartCoreThread();
        this.executorService = executorService;
    }

    private ThreadFactory threadFactory() {
        return new ThreadFactoryBuilder()
            .setNameFormat("DbWorkerThread-%d")
            .build();
    }

    @Override
    public ExecutorService get() {
        return executorService;
    }

}
