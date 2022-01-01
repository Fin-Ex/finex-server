package ru.finex.ws.command.impl.physics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.MutablePairObjectFactory;
import ru.finex.ws.command.PhysicsCommandQueue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PhysicsCommandQueueImpl extends AbstractCommandQueue<AbstractGameObjectCommand, PhysicsCommandContext>
    implements PhysicsCommandQueue {

    @Getter
    private final ObjectPool<Pair<AbstractGameObjectCommand, PhysicsCommandContext>> pairPool = new GenericObjectPool<>(
        new MutablePairObjectFactory<>()
    );

    @Getter
    private final PhysicsCommandScope commandScope;

    @Override
    protected Deque<Pair<AbstractGameObjectCommand, PhysicsCommandContext>> createQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected Queue<Pair<AbstractGameObjectCommand, PhysicsCommandContext>> createPostQueue() {
        return new ArrayDeque<>(256);
    }

    @Override
    protected int getLimit() {
        return 256;
    }

}
