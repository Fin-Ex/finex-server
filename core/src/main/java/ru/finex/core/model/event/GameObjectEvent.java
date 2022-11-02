package ru.finex.core.model.event;

import ru.finex.core.pool.Cleanable;

/**
 * @author m0nster.mind
 */
public interface GameObjectEvent extends Cleanable {

    String CHANNEL = "ru.finex.core.object.GameObject";

    /**
     * Game object runtime ID.
     * @return ID
     */
    int getRuntimeId();

    @Override
    default void clear() {
        // empty
    }

}
