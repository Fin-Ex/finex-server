package ru.finex.ws.player;

import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
public interface GameObjectFactory {

    GameObject createGameObject(String templateName, int persistenceId);

}
