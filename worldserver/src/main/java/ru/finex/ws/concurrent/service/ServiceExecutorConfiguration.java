package ru.finex.ws.concurrent.service;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class ServiceExecutorConfiguration {

    private int minimalThreads;
    private int maximalThreads;
    private long keepAlive;

}
