package ru.finex.ws.service.concurrent;

import ru.finex.core.concurrent.ServerExecutorService;
import ru.finex.ws.concurrent.game.CallableGameTask;
import ru.finex.ws.concurrent.game.RunnableGameTask;

/**
 * @author m0nster.mind
 */
public interface GameExecutorService extends ServerExecutorService<RunnableGameTask, CallableGameTask<?>> {

}
