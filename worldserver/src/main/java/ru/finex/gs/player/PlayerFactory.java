package ru.finex.gs.player;

import ru.finex.core.model.GameObject;
import ru.finex.gs.model.Client;

/**
 * @author m0nster.mind
 */
public interface PlayerFactory {

    GameObject createPlayer(Client client, int persistenceId);

}
