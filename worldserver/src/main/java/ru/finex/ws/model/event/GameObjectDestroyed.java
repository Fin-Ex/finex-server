package ru.finex.ws.model.event;

import lombok.Data;
import ru.finex.core.model.GameObject;
import ru.finex.core.model.GameObjectEvent;

/**
 * @author m0nster.mind
 */
@Data
public class GameObjectDestroyed implements GameObjectEvent {

    private GameObject gameObject;

    @Override
    public void clear() {
        gameObject = null;
    }
}
