package ru.finex.ws.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.finex.ws.network.GameSession;

/**
 * @author m0nster.mind
 */
@Data
@AllArgsConstructor
public class SessionConnected implements GameSessionEvent {

    private GameSession session;

}
