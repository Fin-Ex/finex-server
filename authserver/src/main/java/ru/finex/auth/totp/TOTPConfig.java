package ru.finex.auth.totp;

import dev.samstevens.totp.code.HashingAlgorithm;
import lombok.Data;
import ru.finex.core.hocon.ConfigResource;

/**
 * @author m0nster.mind
 */
@Data
@ConfigResource
public class TOTPConfig {

    public static enum TimeProviderType {
        LOCAL,
        NTP
    }

    private TimeProviderType timeProviderType;
    private String ntpHost;
    private int ntpRefreshTimeMillis;

    private HashingAlgorithm codeGeneratorHash;
    private long codeTimeoutSecs;
    private int codeLength;

}
