package ru.finex.auth.service;

import com.google.inject.ImplementedBy;
import ru.finex.auth.service.impl.TOTPServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(TOTPServiceImpl.class)
public interface TOTPService {

    /**
     * Generate users secret.
     * @return user secret
     */
    String generateSecret();

    /**
     * Verify TOTP code.
     * @param code code
     * @param secret user secret
     * @return true if code is valid, otherwise false
     */
    boolean verifyCode(String code, String secret);

    /**
     * Verify recovery code.
     * Deletes valid code.
     *
     * @param userId user ID
     * @param code recovery code
     * @return true if code is valid, otherwise false
     */
    boolean verifyRecoveryCode(Long userId, String code);

    /**
     * Generate a recovery codes for specified user.
     * @param userId user ID
     */
    void generateRecoveryCodes(Long userId);

    /**
     * Delete all recovery codes for specified user.
     * @param userId user ID
     */
    void deleteRecoveryCodes(Long userId);

}
