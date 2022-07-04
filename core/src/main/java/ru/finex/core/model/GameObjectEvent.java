package ru.finex.core.model;

import ru.finex.core.model.object.GameObject;
import ru.finex.core.pool.Cleanable;

/**
 * @author m0nster.mind
 */
public interface GameObjectEvent extends Cleanable {

    String CHANNEL = "ru.finex.core.model.GameObject";

    /**
     * Игровой объект, который является инициатором события.
     * @return игровой объект
     */
    GameObject getGameObject();

}
