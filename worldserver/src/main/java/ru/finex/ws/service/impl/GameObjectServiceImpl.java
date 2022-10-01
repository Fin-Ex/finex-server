package ru.finex.ws.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.finex.core.cluster.Map;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.core.pool.PoolService;
import ru.finex.ws.model.event.GameObjectDestroyed;
import ru.finex.ws.object.GameObjectFactory;
import ru.finex.ws.service.GameObjectService;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectFactory gameObjectFactory;
    private final PoolService poolService;

    @Clustered
    private Map<Integer, GameObject> gameObjects;

    @Getter
    @Clustered(GameObjectEvent.CHANNEL)
    private ClusterEventBus<GameObjectEvent> eventBus;

    @Override
    public GameObject createGameObject(String template, int persistenceId) {
        GameObject gameObject = gameObjectFactory.createGameObject(template, persistenceId);
        Objects.requireNonNull(gameObject, "Game object is null");
        gameObjects.fastPut(gameObject.getRuntimeId(), gameObject);
        return gameObject;
    }

    @Override
    public GameObject getGameObject(int runtimeId) {
        return gameObjects.get(runtimeId);
    }

    @Override
    public void destroyObject(GameObject gameObject) {
        GameObjectDestroyed event = poolService.getObject(GameObjectDestroyed.class);
        event.setGameObject(gameObject);
        eventBus.notify(event); // m0nster.mind: didnt return object to pool, notify is async
        gameObjects.fastRemove(gameObject.getRuntimeId());
    }

}
