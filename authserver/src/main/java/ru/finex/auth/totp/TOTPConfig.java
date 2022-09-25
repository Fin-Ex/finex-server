package ru.finex.auth.totp;

import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class TOTPConfig {

    private String timeProviderType;
    @ConfigResource(nullable = true)
    private String ntpHost;
    private int ntpRefreshTimeMillis;

    private String codeGeneratorHash;
    private long codeTimeoutSecs;
    private int codeLength;

}
