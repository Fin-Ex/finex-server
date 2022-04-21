package ru.finex.core.repository;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class RepositoryExecutorConfig {

    private int minThreads;
    private int maxThreads;
    private long keepAliveSecs;

}
