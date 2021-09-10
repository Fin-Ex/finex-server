package ru.finex.ws.service.concurrent;

import ru.finex.core.concurrent.ServerExecutorService;
import ru.finex.ws.concurrent.service.CallableServiceTask;
import ru.finex.ws.concurrent.service.RunnableServiceTask;

/**
 * @author m0nster.mind
 */
public interface ServiceExecutorService extends ServerExecutorService<RunnableServiceTask, CallableServiceTask<?>> {

}
