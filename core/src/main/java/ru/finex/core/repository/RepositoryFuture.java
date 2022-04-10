package ru.finex.core.repository;

import lombok.Data;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class RepositoryFuture<T> implements Future<T> {

    public static final int RUNNING = 0;
    public static final int DONE = 1;
    public static final int CANCEL = 2;
    public static final int ERROR = 3;

    private AtomicInteger status = new AtomicInteger(RUNNING);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        //FIXME: realization of cancel !mayInterruptIfRunning
        return false;
    }

    @Override
    public boolean isCancelled() {
        return status.get() == CANCEL;
    }

    public boolean isDone() {
        return status.get() == DONE;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
