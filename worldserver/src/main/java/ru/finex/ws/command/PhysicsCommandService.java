package ru.finex.ws.command;

import com.google.inject.ImplementedBy;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandService;
import ru.finex.ws.command.impl.PhysicsCommandServiceImpl;

/**
 * Executes a once-commands at
 *  {@link ru.finex.ws.tick.TickStage#PRE_PHYSICS PRE_PHYSICS},
 *  {@link ru.finex.ws.tick.TickStage#PHYSICS PHYSICS}
 *  {@link ru.finex.ws.tick.TickStage#POST_PHYSICS POST_PHYSICS}
 *  stages.
 *
 * @author m0nster.mind
 */
@ImplementedBy(PhysicsCommandServiceImpl.class)
public interface PhysicsCommandService extends CommandService<AbstractGameObjectCommand> {
}
