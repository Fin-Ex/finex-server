package ru.finex.ws.command;

import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandQueue;
import ru.finex.core.tick.TickStage;
import ru.finex.ws.command.impl.physics.PhysicsCommandContext;

/**
 * Executes a once-commands at
 *  {@link TickStage#PRE_PHYSICS PRE_PHYSICS},
 *  {@link TickStage#PHYSICS PHYSICS}
 *  {@link TickStage#POST_PHYSICS POST_PHYSICS}
 *  stages.
 *
 * @author m0nster.mind
 */
public interface PhysicsCommandQueue extends CommandQueue<AbstractGameObjectCommand, PhysicsCommandContext> {
}
