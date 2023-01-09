package ru.finex.relay.network;

import ru.finex.network.netty.model.NetworkDto;

/**
 * @author m0nster.mind
 */
public interface RelayNetworkDto extends NetworkDto {

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

}
