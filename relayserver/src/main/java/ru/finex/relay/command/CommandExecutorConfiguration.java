package ru.finex.relay.command;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * Network command thread pool configuration. HOCON representation.
 * @author m0nster.mind
 * @see CommandExecutorProvider
 */
@Data
@ConfigResource
public class CommandExecutorConfiguration {

    private int minimalThreads;
    private int maximalThreads;
    private long keepAlive;

}
