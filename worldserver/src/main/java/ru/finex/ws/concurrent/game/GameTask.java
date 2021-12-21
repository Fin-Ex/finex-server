package ru.finex.ws.concurrent.game;

import ru.finex.core.model.GameObject;
import ru.finex.ws.model.ClientSession;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public interface GameTask {

    ClientSession getClient();

    GameObject getGameObject();

}
