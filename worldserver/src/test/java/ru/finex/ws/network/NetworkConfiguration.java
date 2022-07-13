package ru.finex.ws.network;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class NetworkConfiguration {

    private int acceptorThreads;
    private int clientThreads;

    private String host;
    private int port;

}
