package ru.finex.core.command;

/**
 * Скоуп выполнения команды.
 *
 * @param <T> generic command context
 * @author m0nster.mind
 */
public interface CommandScope<T extends CommandContext> {

    /**
     * Enter to command scope.
     * Provides context to injector command scope.
     * @param context command context
     */
    void enterScope(T context);

    /**
     * Exit from command scope.
     * Removes context from injector command scope.
     */
    void exitScope();

}
