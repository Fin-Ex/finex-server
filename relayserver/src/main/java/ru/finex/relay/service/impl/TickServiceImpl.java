package ru.finex.relay.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.tick.TickService;
import ru.finex.core.tick.TickStage;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class TickServiceImpl implements TickService {

    private final ExecutorService executorService;
    private final NetworkCommandQueue inputCommandService;

    @Inject
    public TickServiceImpl(NetworkCommandQueue inputCommandService) {
        this.inputCommandService = inputCommandService;
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            2,
            new ThreadFactoryBuilder()
                .setNameFormat("TickPool-%d")
                .build()
        );
        executorService.prestartCoreThread();
        this.executorService = executorService;
    }

    @PostConstruct
    private void start() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) this.executorService;
        for (int i = 0; i < executorService.getCorePoolSize(); i++) {
            executorService.submit(this::tick);
        }
    }

    @PreDestroy
    private void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            //
        }
    }

    @Override
    public void register(Object instance, Method method, TickStage stage) {
        throw new NotImplementedException();
    }

    @Override
    public void tick() {
        for (; ; ) {
            if (Thread.interrupted()) {
                break;
            }

            try {
                inputCommandService.executeCommands();
            } catch (Exception e) {
                log.error("Fail to execute commands", e);
            }
        }
    }

    @Override
    public float getDeltaTime() {
        return 0;
    }

    @Override
    public long getDeltaTimeMillis() {
        return 0;
    }

    @Override
    public TickStage getTickStage() {
        return TickStage.INPUT;
    }

}
