package ru.finex.relay.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.NetworkCommandQueue;
import ru.finex.core.command.network.NetworkCommandContext;
import ru.finex.core.command.network.NetworkCommandScope;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Relay network command queue.
 * <p/>
 * Doesn't execute any command, just pushing all normal commands into network command executor as
 *  {@link CommandDecorator command decorator}.
 * <p/>
 * Compatibility:
 * <li><b>{@link #executeCommands()}</b> - not supported</li>
 * <li><b>{@link #executeCommands(int)}</b> - not supported</li>
 * <li><b>{@link #offerPreCommand(AbstractNetworkCommand, NetworkCommandContext)}</b> - not supported</li>
 * <li><b>{@link #offerCommand(AbstractNetworkCommand, NetworkCommandContext)}</b> - supported</li>
 * <li><b>{@link #offerPostCommand(AbstractNetworkCommand, NetworkCommandContext)}</b> - not supported</li>
 *
 * @author m0nster.mind
 * @see CommandExecutorProvider
 * @see CommandDecorator
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class RelayCommandQueue extends AbstractCommandQueue<AbstractNetworkCommand, NetworkCommandContext> implements NetworkCommandQueue {

    @Getter(AccessLevel.PROTECTED)
    private final NetworkCommandScope commandScope;
    private final ExecutorService executorService;

    @Override
    public void executeCommands(int limit) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public void offerPreCommand(AbstractNetworkCommand command, NetworkCommandContext context) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public void offerCommand(AbstractNetworkCommand command, NetworkCommandContext context) {
        CommandDecorator decorator = new CommandDecorator(getCommandScope(), command, context);
        executorService.execute(decorator);
    }

    @Override
    public void offerPostCommand(AbstractNetworkCommand command, NetworkCommandContext context) {
        throw new NotImplementedException("Not supported");
    }

    @Override
    protected Deque<Pair<AbstractNetworkCommand, NetworkCommandContext>> createQueue() {
        return null;
    }

    @Override
    protected Queue<Pair<AbstractNetworkCommand, NetworkCommandContext>> createPostQueue() {
        return null;
    }

    @Override
    protected int getLimit() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    protected ObjectPool<Pair<AbstractNetworkCommand, NetworkCommandContext>> getPairPool() {
        return null;
    }

}
