package ru.finex.core.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author m0nster.mind
 */
public interface ServerExecutorService<Runnable extends RunnableServerTask, Callable extends CallableServerTask<?>> {

    Future<?> execute(Runnable task);
    Future<?> execute(Runnable task, long delay, TimeUnit delayUnit);
    ScheduledFuture<?> execute(Runnable task, long delay, long period, TimeUnit timeUnit);

    <R> Future<R> execute(Callable task);
    <R> Future<R> execute(Callable task, long delay, TimeUnit delayUnit);

}
