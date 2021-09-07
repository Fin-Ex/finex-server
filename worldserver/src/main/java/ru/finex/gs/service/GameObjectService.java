package ru.finex.gs.service;

import ru.finex.core.model.GameObject;
import ru.finex.gs.model.Client;

/**
 * @author m0nster.mind
 */
public interface GameObjectService {

    GameObject createPlayer(Client client, int persistenceId);

    GameObject getGameObject(int runtimeId);

}
