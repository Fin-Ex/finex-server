package ru.finex.ws.player;

import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;

/**
 * @author m0nster.mind
 */
public interface PlayerFactory {

    GameObject createPlayer(Client client, int persistenceId);

}
