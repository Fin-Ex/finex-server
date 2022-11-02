package ru.finex.ws.model.event;

import lombok.Data;
import ru.finex.core.model.event.GameObjectEvent;

/**
 * @author m0nster.mind
 */
@Data
public class GameObjectDestroyed implements GameObjectEvent {

    private int runtimeId;
    
}
