package ru.finex.core.command;

import ru.finex.core.command.network.NetworkCommandContext;

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
public interface NetworkCommandQueue extends CommandQueue<AbstractNetworkCommand, NetworkCommandContext> {
}
