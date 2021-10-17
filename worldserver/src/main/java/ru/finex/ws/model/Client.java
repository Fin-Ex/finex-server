package ru.finex.ws.model;

import ru.finex.core.model.GameObject;
import ru.finex.nif.NetworkClient;

/**
 * @author m0nster.mind
 */
public interface Client extends NetworkClient {

    /**
     * Client logins.
     * @return login name
     */
    String getLogin();

    /**
     * Set client login.
     * @param login login name
     */
    void setLogin(String login);

    /**
     * Determines is client detached.
     * @return true detached, otherwise false
     */
    boolean isDetached();

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

    /**
     * Close client connection.
     * Ignores client traffic and packets.
     */
    void closeNow();

}
