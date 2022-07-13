package ru.finex.ws.model.event;

import ru.finex.ws.network.GameSession;

/**
 * @author m0nster.mind
 */
public interface GameSessionEvent {

    GameSession getSession();

}
