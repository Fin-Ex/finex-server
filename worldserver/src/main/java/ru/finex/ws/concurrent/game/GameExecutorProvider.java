package ru.finex.ws.concurrent.game;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameExecutorProvider implements Provider<ScheduledExecutorService> {

    private GameExecutor executorService;

    @Inject
    public GameExecutorProvider(GameExecutorConfiguration conf) {
        executorService = new GameExecutor(
            conf.getMinimalThreads(),
            new ThreadFactoryBuilder()
                .setNameFormat("GameWorkerThread-%d")
                .setThreadFactory(GameWorkerThread::new)
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
