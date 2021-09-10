package ru.finex.ws.concurrent.service;

import ru.finex.core.concurrent.RunnableServerTask;

/**
 * @author m0nster.mind
 */
public class RunnableServiceTask extends RunnableServerTask implements ServiceTask {

    public RunnableServiceTask(Runnable runnable) {
        super(runnable);
    }

}
