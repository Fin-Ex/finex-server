package ru.finex.ws.concurrent.game;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class GameExecutorConfiguration {

    private int minimalThreads;
    private int maximalThreads;
    private long keepAlive;

}
