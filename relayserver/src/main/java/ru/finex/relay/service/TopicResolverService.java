package ru.finex.relay.service;

import ru.finex.core.model.event.RelayEvent;
import ru.finex.network.netty.model.ClientSession;

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
    String resolve(ClientSession session, RelayEvent dto);

}
