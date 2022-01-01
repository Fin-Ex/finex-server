package ru.finex.ws.command.impl.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.MutablePairObjectFactory;
import ru.finex.ws.command.UpdateCommandQueue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MagicNumber")
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class UpdateCommandQueueImpl extends AbstractCommandQueue<AbstractGameObjectCommand, UpdateCommandContext>
    implements UpdateCommandQueue {

    @Getter
    private final ObjectPool<Pair<AbstractGameObjectCommand, UpdateCommandContext>> pairPool = new GenericObjectPool<>(
        new MutablePairObjectFactory<>()
    );

    @Getter
    private final UpdateCommandScope commandScope;

    @Override
    protected Deque<Pair<AbstractGameObjectCommand, UpdateCommandContext>> createQueue() {
        return new ArrayDeque<>(2048);
    }

    @Override
    protected Queue<Pair<AbstractGameObjectCommand, UpdateCommandContext>> createPostQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected int getLimit() {
        return 1024;
    }

}
