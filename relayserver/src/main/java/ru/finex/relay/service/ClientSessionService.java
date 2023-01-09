package ru.finex.relay.service;

import ru.finex.network.netty.model.ClientSession;
import ru.finex.relay.network.RelayNetworkDto;

/**
 * @author m0nster.mind
 */
public interface ClientSessionService {

    /**
     * Resolve a topic name for specified session and payload.
     * @param session client session
     * @param dto payload
     * @return topic name
     */
    String getServerTopic(ClientSession session, RelayNetworkDto dto);

    /**
     * Register session in session storage.
     * <p>This method allocate a new session ID and bind session to it.
     * @param session client session
     */
    void registerSession(ClientSession session);

    /**
     * Unregister session.
     * @param session client session
     */
    void unregisterSession(ClientSession session);

    /**
     * Trying to get session ID if it allocated.
     * @param session client session
     * @return session ID
     * @exception NullPointerException session ID is not allocated (session is not registered)
     */
    int getSessionId(ClientSession session);

    /**
     * Trying to get session by ID.
     * <p>Session can be not found in current relay instance!
     * @param sessionId session ID
     * @return session or null if not found
     */
    ClientSession getSession(int sessionId);

}
