package ru.finex.ws.command;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractGameObjectCommand;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.inject.Singleton;

/**
 * FIXME m0nster.mind: переделать
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class InputCommandServiceImpl implements InputCommandService {

    private static final int DEFAULT_LIMIT = 512;
    private final BlockingQueue<AbstractGameObjectCommand> commands = new ArrayBlockingQueue<>(2048);

    @Override
    public void executeCommands() {
        executeCommands(DEFAULT_LIMIT);
    }

    @Override
    public void executeCommands(int limit) {
        for (int i = 0; i < limit; i++) {
            AbstractGameObjectCommand command = commands.poll();
            if (command == null) {
                break;
            }

            executeCommand(command);
        }
    }

    private void executeCommand(AbstractGameObjectCommand command) {
        try {
            command.executeCommand();
        } catch (Exception e) {
            log.error("Failed to execute command: {}", command);
        }
    }

    @Override
    public void offerCommand(AbstractGameObjectCommand command) {
        commands.offer(command);
    }

}
