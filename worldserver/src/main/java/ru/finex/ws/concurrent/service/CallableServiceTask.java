package ru.finex.ws.concurrent.service;

import ru.finex.core.concurrent.CallableServerTask;

import java.util.concurrent.Callable;

/**
 * @author m0nster.mind
 */
public class CallableServiceTask<T> extends CallableServerTask<T> implements ServiceTask {

    public CallableServiceTask(Callable<T> callable) {
        super(callable);
    }

}
