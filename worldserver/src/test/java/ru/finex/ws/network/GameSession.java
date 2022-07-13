package ru.finex.ws.network;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.network.AbstractClientSession;
import ru.finex.core.network.NetworkCommandScoped;
import ru.finex.ws.model.ClientSession;
import ru.finex.ws.model.event.GameSessionEvent;
import ru.finex.ws.model.event.SessionConnected;
import ru.finex.ws.model.event.SessionDisconnected;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@NetworkCommandScoped
public class GameSession extends AbstractClientSession implements ClientSession {

    /** Attached game object to session. */
    @Getter
    @Setter
    private GameObject gameObject;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private NetworkCommandQueue commandQueue;

    @Inject
    private EventBus<GameSessionEvent> eventBus;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        eventBus.notify(new SessionConnected(this));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        eventBus.notify(new SessionDisconnected(this));
    }


}
