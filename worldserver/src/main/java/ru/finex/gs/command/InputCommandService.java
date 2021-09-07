package ru.finex.gs.command;

import ru.finex.core.command.AbstractGameObjectCommand;

/**
 * @author m0nster.mind
 */
public interface InputCommandService {

    void executeCommands();
    void executeCommands(int limit);

    void offerCommand(AbstractGameObjectCommand command);

}
