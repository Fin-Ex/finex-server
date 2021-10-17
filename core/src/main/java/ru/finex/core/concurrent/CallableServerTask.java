package ru.finex.core.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @param <T> результат выполнения задачи
 * @author m0nster.mind
 */
@Slf4j
public class CallableServerTask<T> extends ServerTask implements Callable<T> {

    private final Callable<T> callable;

    public CallableServerTask(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public T call() throws Exception {
        try {
            return callable.call();
        } catch (Exception e) {
            log.error("Failed to execute task", e);
            throw new RuntimeException(e);
        }
    }
}
