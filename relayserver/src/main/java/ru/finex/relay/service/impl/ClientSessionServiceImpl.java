package ru.finex.relay.service.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.core.uid.RuntimeIdService;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.relay.network.RelayNetworkDto;
import ru.finex.relay.service.ClientSessionService;
import ru.finex.relay.service.TopicResolverService;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ClientSessionServiceImpl implements ClientSessionService {

    public static final String CTX_SESSION_ID = "ClientSessionId";

    private final Map<Integer, ClientSession> sessions = new ConcurrentHashMap<>();

    private final RuntimeIdService idService;
    private final TopicResolverService topicResolver;

    @Override
    public String getServerTopic(ClientSession session, RelayNetworkDto dto) {
        return topicResolver.resolve(session, dto);
    }

    @Override
    public void registerSession(ClientSession session) {
        int sessionId = idService.generateId();
        session.putContext(CTX_SESSION_ID, sessionId);

        // TODO m0nster.mind: check to duplicate/replace
        sessions.put(sessionId, session);
    }

    @Override
    public void unregisterSession(ClientSession session) {
        sessions.remove(getSessionId(session));
    }

    @Override
    public int getSessionId(ClientSession session) {
        return Objects.requireNonNull((Integer) session.getContext().get(CTX_SESSION_ID),
            "SessionID for " + session + " not found!");
    }

    @Override
    public ClientSession getSession(int sessionId) {
        return sessions.get(sessionId);
    }

}
