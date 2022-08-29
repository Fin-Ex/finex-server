package ru.finex.auth.service.impl;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.NtpTimeProvider;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import jakarta.transaction.Transactional;
import ru.finex.auth.model.entity.TOTPRecoveryCodeEntity;
import ru.finex.auth.repository.TOTPRecoveryCodeRepository;
import ru.finex.auth.service.TOTPService;
import ru.finex.auth.totp.RecoveryCodeGenerator;
import ru.finex.auth.totp.SecretGeneratorImpl;
import ru.finex.auth.totp.TOTPConfig;

import java.net.UnknownHostException;
import java.util.Objects;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class TOTPServiceImpl implements TOTPService {

    private final SecretGenerator generator = new SecretGeneratorImpl(32);
    private final RecoveryCodeGenerator recoveryCodeGenerator = new RecoveryCodeGenerator();
    private final CodeVerifier codeVerifier;
    private final TOTPRecoveryCodeRepository recoveryCodeRepository;

    @Inject
    public TOTPServiceImpl(TOTPConfig config, TOTPRecoveryCodeRepository recoveryCodeRepository) {
        codeVerifier = new DefaultCodeVerifier(createCodeGenerator(config), createTimeProvider(config));
        this.recoveryCodeRepository = recoveryCodeRepository;
    }

    private TimeProvider createTimeProvider(TOTPConfig config) {
        return switch (config.getTimeProviderType()) {
            case LOCAL -> new SystemTimeProvider();
            case NTP -> {
                try {
                    yield new NtpTimeProvider(config.getNtpHost(), config.getNtpRefreshTimeMillis());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private CodeGenerator createCodeGenerator(TOTPConfig config) {
        return new DefaultCodeGenerator(config.getCodeGeneratorHash(), config.getCodeLength());
    }

    @Override
    public String generateSecret() {
        return generator.generate();
    }

    @Override
    public boolean verifyCode(String code, String secret) {
        return codeVerifier.isValidCode(secret, code);
    }

    @Transactional
    @Override
    public boolean verifyRecoveryCode(Long userId, String code) {
        return recoveryCodeRepository.deleteByUserIdAndCode(userId, code);
    }

    @Transactional
    @Override
    public void generateRecoveryCodes(Long userId) {
        Objects.requireNonNull(userId, "UserID is null!");

        Stream.of(recoveryCodeGenerator.generateCodes())
            .map(code -> TOTPRecoveryCodeEntity.builder()
                .userId(userId)
                .code(code)
                .build()
            ).forEach(recoveryCodeRepository::create);
    }

    @Transactional
    @Override
    public void deleteRecoveryCodes(Long userId) {
        Objects.requireNonNull(userId, "UserID is null!");

        recoveryCodeRepository.deleteByUserId(userId);
    }
}
