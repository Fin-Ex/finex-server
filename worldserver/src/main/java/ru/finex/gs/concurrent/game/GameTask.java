package ru.finex.gs.concurrent.game;

import ru.finex.core.model.GameObject;
import ru.finex.gs.model.Client;

/**
 * @author m0nster.mind
 */
public interface GameTask {

    Client getClient();
    GameObject getGameObject();

}
