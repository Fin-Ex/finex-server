package ru.finex.ws.command.impl;

import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.ws.command.PhysicsCommandQueue;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PhysicsCommandQueueImpl extends AbstractCommandQueue<AbstractGameObjectCommand>
    implements PhysicsCommandQueue {

    @Override
    protected Queue<AbstractGameObjectCommand> createPreQueue() {
        return new ArrayDeque<>(256);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createPostQueue() {
        return new ArrayDeque<>(256);
    }

    @Override
    protected int getLimit() {
        return 256;
    }

}
