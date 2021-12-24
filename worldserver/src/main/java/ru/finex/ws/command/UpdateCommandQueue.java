package ru.finex.ws.command;

import com.google.inject.ImplementedBy;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandQueue;
import ru.finex.ws.command.impl.UpdateCommandQueueImpl;

/**
 * Executes a once-commands at
 *  {@link ru.finex.ws.tick.TickStage#PRE_UPDATE PRE_UPDATE},
 *  {@link ru.finex.ws.tick.TickStage#UPDATE UPDATE}
 *  {@link ru.finex.ws.tick.TickStage#POST_UPDATE POST_UPDATE}
 *  stages.
 *
 * @author m0nster.mind
 */
@ImplementedBy(UpdateCommandQueueImpl.class)
public interface UpdateCommandQueue extends CommandQueue<AbstractGameObjectCommand> {
}
