package ru.finex.ws.service.impl;

import com.hazelcast.core.HazelcastInstance;
import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;
import ru.finex.ws.player.GameObjectFactory;
import ru.finex.ws.player.PlayerFactory;
import ru.finex.ws.service.GameObjectService;

import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectFactory gameObjectFactory;
    private final PlayerFactory playerFactory;
    private final Map<Integer, GameObject> gameObjects;

    @Inject
    public GameObjectServiceImpl(GameObjectFactory gameObjectFactory, PlayerFactory playerFactory, HazelcastInstance hazelcast) {
        this.gameObjectFactory = gameObjectFactory;
        this.playerFactory = playerFactory;
        this.gameObjects = hazelcast.getMap(getClass().getCanonicalName() + "#gameObjects");
    }

    @Override
    public GameObject createPlayer(Client client, int persistenceId) {
        GameObject player = playerFactory.createPlayer(client, persistenceId);
        Objects.requireNonNull(player, "Player is null");
        gameObjects.put(player.getRuntimeId(), player);
        return player;
    }

    @Override
    public GameObject createGameObject(String template, int persistenceId) {
        GameObject gameObject = gameObjectFactory.createGameObject(template, persistenceId);
        Objects.requireNonNull(gameObject, "Game object is null");
        gameObjects.put(gameObject.getRuntimeId(), gameObject);
        return gameObject;
    }

    @Override
    public GameObject getGameObject(int runtimeId) {
        return gameObjects.get(runtimeId);
    }
}
