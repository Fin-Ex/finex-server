package ru.finex.ws.player;

import ru.finex.core.model.GameObject;

/**
 * @author finfan
 */
public interface GameObjectFactory {

    GameObject createGameObject(String templateName, int persistenceId);

}
