package ru.finex.gs.model;

import ru.finex.core.model.GameObject;
import ru.finex.nif.NetworkClient;

/**
 * @author m0nster.mind
 */
public interface Client extends NetworkClient {

    String getLogin();
    void setLogin(String login);

    boolean isDetached();

    GameObject getGameObject();
    void setGameObject(GameObject gameObject);

    void closeNow();

}
