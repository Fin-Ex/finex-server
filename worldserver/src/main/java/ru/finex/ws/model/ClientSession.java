package ru.finex.ws.model;

import ru.finex.core.model.object.GameObject;

/**
 * @author m0nster.mind
 */
public interface ClientSession extends ru.finex.network.netty.model.ClientSession {

    /**
     * Client game object.
     * @return game object
     */
    GameObject getGameObject();

    /**
     * Set client game object.
     * @param gameObject game object
     */
    void setGameObject(GameObject gameObject);

}
