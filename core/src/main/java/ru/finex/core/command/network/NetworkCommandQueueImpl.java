package ru.finex.core.command.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.MutablePairObjectFactory;
import ru.finex.core.command.NetworkCommandQueue;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Реализация сетевой очереди команд.
 *
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkCommandQueueImpl extends AbstractCommandQueue<AbstractNetworkCommand, NetworkCommandContext>
    implements NetworkCommandQueue {

    private static final int DEFAULT_LIMIT = 512;

    @Getter
    private final ObjectPool<Pair<AbstractNetworkCommand, NetworkCommandContext>> pairPool = PoolUtils.synchronizedPool(
        new GenericObjectPool<>(new MutablePairObjectFactory<>())
    );

    @Getter
    private final NetworkCommandScope commandScope;

    @Override
    protected Deque<Pair<AbstractNetworkCommand, NetworkCommandContext>> createQueue() {
        return new ConcurrentLinkedDeque<>();
    }

    @Override
    protected Queue<Pair<AbstractNetworkCommand, NetworkCommandContext>> createPostQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Override
    protected int getLimit() {
        return DEFAULT_LIMIT;
    }

}
