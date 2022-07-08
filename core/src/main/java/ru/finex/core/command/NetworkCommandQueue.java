package ru.finex.core.command;

import ru.finex.core.command.network.NetworkCommandContext;

/**
 * @author m0nster.mind
 */
public interface NetworkCommandQueue extends CommandQueue<AbstractNetworkCommand, NetworkCommandContext> {
}
