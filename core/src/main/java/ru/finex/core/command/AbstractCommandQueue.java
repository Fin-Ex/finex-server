package ru.finex.core.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.ObjectPool;

import java.util.Deque;
import java.util.Queue;

/**
 * @param <T> generic command
 * @param <C> generic command context
 * @author m0nster.mind
 */
@Slf4j
public abstract class AbstractCommandQueue<T extends Command, C extends CommandContext> implements CommandQueue<T, C> {

    private final Deque<Pair<T, C>> commands = createQueue();
    private final Queue<Pair<T, C>> postCommands = createPostQueue();

    @Override
    public void executeCommands() {
        executeCommands(getLimit());
    }

    @Override
    public void executeCommands(int limit) {
        ObjectPool<Pair<T, C>> pairPool = getPairPool();
        CommandScope<C> commandScope = getCommandScope();
        for (int commandsExecuted = 0; commandsExecuted < limit; commandsExecuted++) {
            Pair<T, C> pair = commands.poll();
            if (pair == null) {
                pair = postCommands.poll();
                if (pair == null) {
                    break;
                }
            }

            commandScope.enterScope(pair.getValue());
            executeCommand(pair);
            commandScope.exitScope(); // XXX m0nster.mind: по текущей логике [2021.12.24] можно вынести вниз, чтобы не дергать постоянно тредлокал
            try {
                pairPool.returnObject(pair);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void executeCommand(Pair<T, C> pair) {
        T command = pair.getKey();
        try {
            command.executeCommand();
        } catch (Exception e) {
            log.error("Failed to execute command: {}", command);
        }
    }

    @Override
    public void offerPreCommand(T command, C context) {
        commands.offerFirst(getPair(command, context));
    }

    @Override
    public void offerCommand(T command, C context) {
        commands.offer(getPair(command, context));
    }

    @Override
    public void offerPostCommand(T command, C context) {
        postCommands.offer(getPair(command, context));
    }

    private Pair<T, C> getPair(T command, C context) {
        MutablePair<T, C> pair;
        try {
            pair = (MutablePair<T, C>) getPairPool().borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        pair.setLeft(command);
        pair.setRight(context);
        return pair;
    }

    protected abstract CommandScope<C> getCommandScope();

    protected abstract Deque<Pair<T, C>> createQueue();

    protected abstract Queue<Pair<T, C>> createPostQueue();

    protected abstract int getLimit();

    protected abstract ObjectPool<Pair<T, C>> getPairPool();

}
