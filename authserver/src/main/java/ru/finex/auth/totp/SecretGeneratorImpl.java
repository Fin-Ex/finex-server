package ru.finex.auth.totp;

import dev.samstevens.totp.secret.SecretGenerator;
import ru.finex.core.rng.RandomGenerator;
import ru.finex.core.rng.RandomProviders;

import java.util.Base64;

/**
 * @author m0nster.mind
 */
public class SecretGeneratorImpl implements SecretGenerator {

    private final int numCharacters;

    public SecretGeneratorImpl() {
        this.numCharacters = 32;
    }

    /**
     * @param numCharacters The number of characters the secret should consist of.
     */
    public SecretGeneratorImpl(int numCharacters) {
        this.numCharacters = numCharacters;
    }

    @Override
    public String generate() {
        return new String(Base64.getEncoder().encode(getRandomBytes()));
    }

    private byte[] getRandomBytes() {
        // 5 bits per char in base32
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        RandomGenerator secureRandom = RandomProviders.secureRandom().get();
        secureRandom.nextBytes(bytes);

        return bytes;
    }

}
