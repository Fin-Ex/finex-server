package ru.finex.ws.command.impl;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractCommandService;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.ws.command.InputCommandService;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class InputCommandServiceImpl extends AbstractCommandService<AbstractGameObjectCommand>
    implements InputCommandService {

    private static final int DEFAULT_LIMIT = 512;

    @Override
    protected Queue<AbstractGameObjectCommand> createPreQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createQueue() {
        return new ArrayBlockingQueue<>(2048);
    }

    @Override
    protected Queue<AbstractGameObjectCommand> createPostQueue() {
        return new ArrayDeque<>(512);
    }

    @Override
    protected int getLimit() {
        return DEFAULT_LIMIT;
    }

}