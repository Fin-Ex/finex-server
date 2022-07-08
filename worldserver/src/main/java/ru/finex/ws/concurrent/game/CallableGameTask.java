package ru.finex.ws.concurrent.game;

import lombok.Getter;
import ru.finex.core.concurrent.CallableServerTask;
import ru.finex.core.object.GameObject;
import ru.finex.ws.model.ClientSession;

import java.util.concurrent.Callable;

/**
 * Игровая задача.
 *
 * @param <T> результат выполнения задачи
 * @author m0nster.mind
 */
public class CallableGameTask<T> extends CallableServerTask<T> implements GameTask {

    @Getter
    private final ClientSession client;

    @Getter
    private final GameObject gameObject;

    /**
     * Создать игровую задачу.
     *
     * @param callable задача
     * @param client клиент, который выполняет данную задачу
     */
    public CallableGameTask(Callable<T> callable, ClientSession client) {
        this(callable, client, null);
    }

    /**
     * Создать игровую задачу.
     *
     * @param callable задача
     * @param client клиент, который выполняет данную задачу
     * @param gameObject игрок, который выполняет данную задачу (can be null)
     */
    public CallableGameTask(Callable<T> callable, ClientSession client, GameObject gameObject) {
        super(callable);
        this.client = client;
        if (gameObject == null) {
            this.gameObject = client.getGameObject();
        } else {
            this.gameObject = gameObject;
        }
    }

}
