package ru.finex.ws.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.model.event.GameObjectEvent;
import ru.finex.core.pool.PooledObject;

/**
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = "400")
@NoArgsConstructor
@AllArgsConstructor
public class GameObjectDestroyed implements GameObjectEvent {

    private int runtimeId;
    
}
