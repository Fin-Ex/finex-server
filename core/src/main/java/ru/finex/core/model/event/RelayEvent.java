package ru.finex.core.model.event;

import ru.finex.core.pool.Cleanable;

/**
 * @author m0nster.mind
 */
public interface RelayEvent extends Cleanable {

    /**
     * Return client session ID.
     * @return client session ID
     */
    int getClientSessionId();

    /**
     * Set client session ID.
     * @param clientSessionId client session ID
     */
    void setClientSessionId(int clientSessionId);

    @Override
    default void clear() {
        // nop
    }

}
