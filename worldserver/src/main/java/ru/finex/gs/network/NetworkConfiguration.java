package ru.finex.gs.network;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class NetworkConfiguration {

    private String hostname;
    private int port;
    private int backlog;

}
