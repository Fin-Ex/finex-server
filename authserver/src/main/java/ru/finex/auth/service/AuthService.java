package ru.finex.auth.service;

import com.google.inject.ImplementedBy;
import ru.finex.auth.model.AuthState;
import ru.finex.auth.service.impl.AuthServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(AuthServiceImpl.class)
public interface AuthService {

    /**
     * FIXME write a doc!
     * @param login user login
     * @param password hashed password
     * @return user auth state
     */
    AuthState authUser(String login, String password);

    /**
     * 2FA auth. FIXME write a doc!
     * @param login user login
     * @param totpCode 2FA TOTP code
     * @return user auth state
     */
    AuthState authUserTOTP(String login, String totpCode);

    /**
     * Logout user. FIXME write a doc!
     * @param login user login
     * @return previous user status
     */
    AuthState logoutUser(String login);

}
