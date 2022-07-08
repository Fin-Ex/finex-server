package ru.finex.core.model.event.object.impl;

import lombok.Data;
import ru.finex.core.model.event.object.GameObjectEvent;
import ru.finex.core.object.GameObject;

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
