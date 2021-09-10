package ru.finex.ws.concurrent.game;

import lombok.Getter;
import ru.finex.core.concurrent.CallableServerTask;
import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;

import java.util.concurrent.Callable;

/**
 * Игровая задача.
 *
 * @author m0nster.mind
 */
public class CallableGameTask<T> extends CallableServerTask<T> implements GameTask {

    @Getter
    private final Client client;

    @Getter
    private final GameObject gameObject;

    /**
     * Создать игровую задачу.
     * @param callable задача
     * @param client клиент, который выполняет данную задачу
     */
    public CallableGameTask(Callable<T> callable, Client client) {
        this(callable, client, null);
    }

    /**
     * Создать игровую задачу.
     * @param callable задача
     * @param client клиент, который выполняет данную задачу
     * @param gameObject игрок, который выполняет данную задачу (can be null)
     */
    public CallableGameTask(Callable<T> callable, Client client, GameObject gameObject) {
        super(callable);
        this.client = client;
        if (gameObject == null) {
            this.gameObject = client.getGameObject();
        } else {
            this.gameObject = gameObject;
        }
    }

}
