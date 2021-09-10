package ru.finex.ws.service.impl;

import com.hazelcast.core.HazelcastInstance;
import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;
import ru.finex.ws.service.GameObjectService;
import ru.finex.ws.player.PlayerFactory;

import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameObjectServiceImpl implements GameObjectService {

    private final PlayerFactory playerFactory;
    private final Map<Integer, GameObject> gameObjects;

    @Inject
    public GameObjectServiceImpl(PlayerFactory playerFactory, HazelcastInstance hazelcast) {
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
    public GameObject getGameObject(int runtimeId) {
        return gameObjects.get(runtimeId);
    }
}
