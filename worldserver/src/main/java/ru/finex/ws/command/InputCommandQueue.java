package ru.finex.ws.command;

import com.google.inject.ImplementedBy;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandQueue;
import ru.finex.ws.command.impl.InputCommandQueueImpl;

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
@ImplementedBy(InputCommandQueueImpl.class)
public interface InputCommandQueue extends CommandQueue<AbstractGameObjectCommand> {
}
