package ru.finex.ws.command.impl;

import ru.finex.core.command.AbstractCommandService;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.ws.command.PhysicsCommandService;

import java.util.ArrayDeque;
import java.util.Queue;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class PhysicsCommandServiceImpl extends AbstractCommandService<AbstractGameObjectCommand>
    implements PhysicsCommandService {

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
