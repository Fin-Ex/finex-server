package ru.finex.core.command;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * @author m0nster.mind
 */
@Slf4j
public abstract class AbstractCommandService<T extends Command> implements CommandService<T> {

    private final Queue<T> preCommands = createPreQueue();
    private final Queue<T> commands = createQueue();
    private final Queue<T> postCommands = createPostQueue();

    @Override
    public void executeCommands() {
        executeCommands(getLimit());
    }

    @Override
    public void executeCommands(int limit) {
        for (T command = preCommands.poll(); command != null; command = preCommands.poll()) {
            try {
                executeCommand(command);
            } catch (Exception e) {
                log.error("Fail to process pre-command '{}'", command, e);
            }
        }

        for (int i = 0; i < limit; i++) {
            T command = commands.poll();
            if (command == null) {
                break;
            }

            try {
                executeCommand(command);
            } catch (Exception e) {
                log.error("Fail to process command '{}'", command, e);
            }
        }

        for (T command = postCommands.poll(); command != null; command = postCommands.poll()) {
            try {
                executeCommand(command);
            } catch (Exception e) {
                log.error("Fail to process post-command '{}'", command, e);
            }
        }

    }

    private void executeCommand(T command) {
        try {
            command.executeCommand();
        } catch (Exception e) {
            log.error("Failed to execute command: {}", command);
        }
    }

    @Override
    public void offerPreCommand(T command) {
        preCommands.offer(command);
    }

    @Override
    public void offerCommand(T command) {
        commands.offer(command);
    }

    @Override
    public void offerPostCommand(T command) {
        postCommands.offer(command);
    }

    protected abstract Queue<T> createPreQueue();
    protected abstract Queue<T> createQueue();
    protected abstract Queue<T> createPostQueue();

    protected abstract int getLimit();

}
