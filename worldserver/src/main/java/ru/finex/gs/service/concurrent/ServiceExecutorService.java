package ru.finex.gs.service.concurrent;

import ru.finex.core.concurrent.ServerExecutorService;
import ru.finex.gs.concurrent.service.CallableServiceTask;
import ru.finex.gs.concurrent.service.RunnableServiceTask;

/**
 * @author m0nster.mind
 */
public interface ServiceExecutorService extends ServerExecutorService<RunnableServiceTask, CallableServiceTask<?>> {

}
