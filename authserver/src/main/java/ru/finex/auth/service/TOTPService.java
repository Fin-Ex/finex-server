package ru.finex.auth.service;

import com.google.inject.ImplementedBy;
import ru.finex.auth.service.impl.TOTPServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(TOTPServiceImpl.class)
public interface TOTPService {

    String generateSecret();
    boolean verifyCode(String code, String secret);
    boolean verifyRecoveryCode(Long userId, String code);

    void generateRecoveryCodes(Long userId);
    void deleteRecoveryCodes(Long userId);

}
