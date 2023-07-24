package ru.finex.relay.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.command.CommandScope;
import ru.finex.core.command.network.NetworkCommandContext;

/**
 * Decorates network command to runnable with enter to command scope and exits in run.
 * @author m0nster.mind
 */
@Slf4j
@RequiredArgsConstructor
public class CommandDecorator implements Runnable {

    private final CommandScope<NetworkCommandContext> scope;
    private final AbstractNetworkCommand command;
    private final NetworkCommandContext context;

    @Override
    public void run() {
        scope.enterScope(context);
        try {
            command.executeCommand();
        } catch (Exception e) {
            log.error("Failed to execute command: {}", command, e);
        }
        scope.exitScope();
    }

}
