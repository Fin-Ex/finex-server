package ru.finex.gs.concurrent.service;

import ru.finex.gs.service.concurrent.ServiceExecutorService;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class ServiceExecutorServiceImpl implements ServiceExecutorService {

    @Inject
    @Named("Service")
    private ScheduledExecutorService executorService;

    @Override
    public Future<?> execute(RunnableServiceTask task) {
        return executorService.submit(task);
    }

    @Override
    public Future<?> execute(RunnableServiceTask task, long delay, TimeUnit delayUnit) {
        return executorService.schedule(task, delay, delayUnit);
    }

    @Override
    public ScheduledFuture<?> execute(RunnableServiceTask task, long delay, long period, TimeUnit timeUnit) {
        return executorService.scheduleAtFixedRate(task, delay, period, timeUnit);
    }

    @Override
    public <R> Future<R> execute(CallableServiceTask<?> task) {
        return (Future<R>) executorService.submit(task);
    }

    @Override
    public <R> Future<R> execute(CallableServiceTask<?> task, long delay, TimeUnit delayUnit) {
        return (Future<R>) executorService.schedule(task, delay, delayUnit);
    }

}
