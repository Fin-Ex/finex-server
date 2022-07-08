package ru.finex.core.model.event.object;

import ru.finex.core.object.GameObject;
import ru.finex.core.pool.Cleanable;

/**
 * @author m0nster.mind
 */
public interface GameObjectEvent extends Cleanable {

    String CHANNEL = "ru.finex.core.model.object.GameObject";

    /**
     * Игровой объект, который является инициатором события.
     * @return игровой объект
     */
    GameObject getGameObject();

}
