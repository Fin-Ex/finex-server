package ru.finex.ws.service;

import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
public interface GameObjectService {

    GameObject createPlayer(Client client, int persistenceId);

    GameObject getGameObject(int runtimeId);

}
