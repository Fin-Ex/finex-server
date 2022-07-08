package ru.finex.core.command;

import ru.finex.core.inject.scope.ScopeHandler;

/**
 * Скоуп выполнения команды.
 *
 * @param <T> generic command context
 * @author m0nster.mind
 */
public interface CommandScope<T extends CommandContext> extends ScopeHandler<T> {

}
