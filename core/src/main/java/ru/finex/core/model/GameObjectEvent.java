package ru.finex.core.model;

import ru.finex.core.pool.Cleanable;

/**
 * @author m0nster.mind
 */
public interface GameObjectEvent extends Cleanable {

    String CHANNEL = "ru.finex.core.model.GameObject";

    GameObject getGameObject();

}
