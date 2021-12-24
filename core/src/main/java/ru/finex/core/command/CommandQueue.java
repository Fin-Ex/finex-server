package ru.finex.core.command;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface CommandQueue<T extends Command> {

    void executeCommands();

    void executeCommands(int limit);

    void offerPreCommand(T command);

    void offerCommand(T command);

    void offerPostCommand(T command);

}
