package ru.finex.core.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.command.network.NetworkCommandContext;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.network.netty.serial.PacketDeserializer;

import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Репрезентация игрового клиента.
 *
 * @author m0nster.mind
 */
public abstract class AbstractClientSession
    extends SimpleChannelInboundHandler<Pair<PacketMetadata<PacketDeserializer<?>>, NetworkDto>>
    implements ClientSession {

    /** Login name. */
    @Getter
    @Setter
    private String login;

    @Getter
    private boolean isDetached;

    @Getter(AccessLevel.PROTECTED)
    private Channel channel;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private NetworkCommandService networkCommandService;

    protected abstract NetworkCommandQueue getCommandQueue();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel = ctx.channel();
        isDetached = false;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        isDetached = true;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Pair<PacketMetadata<PacketDeserializer<?>>, NetworkDto> msg) throws Exception {
        NetworkCommandQueue commandQueue = getCommandQueue();
        List<Pair<AbstractNetworkCommand, NetworkCommandContext>> commands = networkCommandService.createCommands(msg.getKey(), msg.getValue(), this);
        for (int i = 0; i < commands.size(); i++) {
            Pair<AbstractNetworkCommand, NetworkCommandContext> pair = commands.get(i);
            commandQueue.offerCommand(pair.getKey(), pair.getValue());
        }
    }

    @Override
    public void sendPacket(NetworkDto dto) {
        if (isDetached) {
            return;
        }

        channel.writeAndFlush(dto);
    }

    @Override
    public void close(NetworkDto dto) {
        isDetached = true;
        channel.writeAndFlush(dto);
        channel.close();
    }

    /**
     * {@inheritDoc}.
     * Blocking method.
     */
    @SneakyThrows
    @Override
    public void closeNow() {
        isDetached = true;
        channel.close().sync();
    }

    @Override
    public String toString() {
        SocketAddress address = channel.remoteAddress();
        if (address == null) {
            return "GameSession(disconnected)";
        }

        return "GameSession(login=" +
            Objects.requireNonNullElse(login, "-") +
            ", address=" + address.toString();
    }

}
