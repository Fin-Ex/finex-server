package ru.finex.core.command;

/**
 * Интерфейс очереди команд.
 * Очередь команд может содержать в себе pre-commands, commands и post-commands.
 * В зависимости от выполнения очереди команд (если установлен лимит), часть команд может не выполнится.
 * Последовательность выполнения команд в очереди: сперва выполняются pre-commands,
 *  потом выполняюстя commands и только потом - post-commands.
 * Таким образом, очередь команд создаёт приоритет выполнения всех команд
 *  (включая и предустановленный лимит, если он существует).
 *
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:JavadocType")
public interface CommandQueue<T extends Command, C extends CommandContext> {

    /**
     * Выполнить все команды в очереди.
     */
    void executeCommands();

    /**
     * Выполнить лимитированное кол-во команд в очереди.
     *
     * @param limit макс. количество команд, которое будет выполнено
     */
    void executeCommands(int limit);

    /**
     * Положить в очередь пре-команду.
     *
     * @param command команда
     * @param context контекст
     */
    void offerPreCommand(T command, C context);

    /**
     * Положить в очередь команду.
     *
     * @param command команда
     * @param context контекст
     */
    void offerCommand(T command, C context);

    /**
     * Положить в очередь пост-команду.
     *
     * @param command команда
     * @param context контекст
     */
    void offerPostCommand(T command, C context);

}
