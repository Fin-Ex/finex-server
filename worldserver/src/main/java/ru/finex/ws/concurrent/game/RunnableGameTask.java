package ru.finex.ws.concurrent.game;

import lombok.Getter;
import ru.finex.core.concurrent.RunnableServerTask;
import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;

/**
 * Игровая задача.
 *
 * @author m0nster.mind
 */
public class RunnableGameTask extends RunnableServerTask implements GameTask {

    @Getter
    private final Client client;

    @Getter
    private final GameObject gameObject;

    /**
     * Создать игровую задачу.
     * @param runnable задача
     * @param client клиент, который выполняет данную задачу
     */
    public RunnableGameTask(Runnable runnable, Client client) {
        this(runnable, client, null);
    }

    /**
     * Создать игровую задачу.
     * @param runnable задача
     * @param client клиент, который выполняет данную задачу
     * @param gameObject игрок, который выполняет данную задачу (can be null)
     */
    public RunnableGameTask(Runnable runnable, Client client, GameObject gameObject) {
        super(runnable);
        this.client = client;
        if (gameObject == null) {
            this.gameObject = client.getGameObject();
        } else {
            this.gameObject = gameObject;
        }
    }
    
    protected RunnableGameTask(Runnable runnable) {
        super(runnable);
        this.client = null;
        this.gameObject = null;
    }
}
