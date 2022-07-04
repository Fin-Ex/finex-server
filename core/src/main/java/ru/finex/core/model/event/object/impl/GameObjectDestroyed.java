package ru.finex.core.model.event.object.impl;

import lombok.Data;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.core.model.object.GameObject;

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
