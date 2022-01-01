package ru.finex.core.command;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface CommandQueue<T extends Command, C extends CommandContext> {

    void executeCommands();

    void executeCommands(int limit);

    void offerPreCommand(T command, C context);

    void offerCommand(T command, C context);

    void offerPostCommand(T command, C context);

}
