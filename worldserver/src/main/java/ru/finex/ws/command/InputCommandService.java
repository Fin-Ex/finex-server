package ru.finex.ws.command;

import com.google.inject.ImplementedBy;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandService;
import ru.finex.ws.command.impl.InputCommandServiceImpl;

/**
 * Executes a once-commands at
 *  {@link ru.finex.ws.tick.TickStage#PRE_INPUT PRE_INPUT},
 *  {@link ru.finex.ws.tick.TickStage#INPUT INPUT}
 *  {@link ru.finex.ws.tick.TickStage#POST_INPUT POST_INPUT}
 *  stages.
 * The normal stage ({@link ru.finex.ws.tick.TickStage#INPUT INPUT}) is blocking.
 *
 * @author m0nster.mind
 */
@ImplementedBy(InputCommandServiceImpl.class)
public interface InputCommandService extends CommandService<AbstractGameObjectCommand> {
}
