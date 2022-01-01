package ru.finex.core.management;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class ManagementConfig {

    private boolean enabled;
    private String host;
    private int port;
    private String path;

}
