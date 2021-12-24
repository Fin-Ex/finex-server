package ru.finex.ws.command.impl;

import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.ws.command.UpdateCommandQueue;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MagicNumber")
@Singleton
public class UpdateCommandQueueImpl extends AbstractCommandQueue<AbstractGameObjectCommand>
    implements UpdateCommandQueue {

    @Override
    protected Queue<AbstractGameObjectCommand> createPreQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createQueue() {
        return new ArrayDeque<>(2048);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createPostQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected int getLimit() {
        return 1024;
    }

}
