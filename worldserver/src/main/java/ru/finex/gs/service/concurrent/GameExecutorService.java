package ru.finex.gs.service.concurrent;

import ru.finex.core.concurrent.ServerExecutorService;
import ru.finex.gs.concurrent.game.CallableGameTask;
import ru.finex.gs.concurrent.game.RunnableGameTask;

/**
 * @author m0nster.mind
 */
public interface GameExecutorService extends ServerExecutorService<RunnableGameTask, CallableGameTask<?>> {

}
