package ru.finex.relay.service;

import ru.finex.network.netty.model.ClientSession;
import ru.finex.relay.network.RelayNetworkDto;

/**
 * @author m0nster.mind
 */
public interface TopicResolverService {

    /**
     * Resolve a topic name for specified session and payload.
     * @param session client session
     * @param dto payload
     * @return topic name
     */
    String resolve(ClientSession session, RelayNetworkDto dto);

}
