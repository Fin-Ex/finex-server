package ru.finex.gs.tick;

import ru.finex.core.command.AbstractGameObjectCommand;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface Tickable {

    List<AbstractGameObjectCommand> getTickCommands();

}
