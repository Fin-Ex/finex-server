package ru.finex.ws.concurrent.game;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author m0nster.mind
 */
public class GameExecutor extends ScheduledThreadPoolExecutor {
    public GameExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public GameExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public GameExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public GameExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        GameWorkerThread thread = (GameWorkerThread) t;
        GameScheduledFutureTask<?> task = (GameScheduledFutureTask<?>) r;
        thread.setClient(task.getClient());
        thread.setGameObject(task.getGameObject());
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        GameTask gameTask = (GameTask) runnable;
        return new GameScheduledFutureTask<>(task, gameTask.getClient(), gameTask.getGameObject());
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        GameTask gameTask = (GameTask) callable;
        return new GameScheduledFutureTask<>(task, gameTask.getClient(), gameTask.getGameObject());
    }
}
