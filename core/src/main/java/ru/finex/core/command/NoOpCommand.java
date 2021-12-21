package ru.finex.core.command;

/**
 * No operation command. Does nothing.
 * Null-object representation of command.
 *
 * @author m0nster.mind
 */
public class NoOpCommand extends AbstractNetworkCommand {

    public static final NoOpCommand INSTANCE = new NoOpCommand();

    @Override
    public void executeCommand() {
        // nop
    }

}
