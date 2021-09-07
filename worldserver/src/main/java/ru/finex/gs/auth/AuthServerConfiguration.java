package ru.finex.gs.auth;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class AuthServerConfiguration {

    private String hostname;
    private int port;

}
