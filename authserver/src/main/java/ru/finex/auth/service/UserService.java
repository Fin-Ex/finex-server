package ru.finex.auth.service;

import com.google.inject.ImplementedBy;
import ru.finex.auth.model.entity.UserEntity;
import ru.finex.auth.model.exception.DuplicateUserException;
import ru.finex.auth.model.exception.TOTPException;
import ru.finex.auth.model.exception.UserNotFoundException;
import ru.finex.auth.service.impl.UserServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    /**
     * Find user by specified login.
     * @param login login
     * @return user or null if not found
     */
    UserEntity getByLogin(String login);

    /**
     * Check user password.
     * If user password stored as hashed, plain password will hash too (with specified algorithm)
     *  before equality check.
     *
     * @param login user login
     * @param password password plain text
     * @return true if passwords equals
     * @exception UserNotFoundException user with specified login not found
     */
    boolean checkPassword(String login, String password) throws UserNotFoundException;

    /**
     * Check user password.
     *
     * @param login user login
     * @param password password, plain text
     * @param hash hash type
     * @return true if passwords equals, otherwise false
     * @exception UserNotFoundException user with specified login not found
     */
    boolean checkPassword(String login, String password, String hash) throws UserNotFoundException;

    /**
     * Creates a new user with specified login and password.
     * @param login login
     * @param password plain password
     * @param hash hash type
     * @exception DuplicateUserException user with specified login already exists
     */
    void createUser(String login, String password, String hash) throws DuplicateUserException;

    /**
     * Generate a new random password for user and send it to.
     * @param login login name
     * @throws UserNotFoundException user with specified login not found
     */
    void restorePassword(String login) throws UserNotFoundException;

    /**
     * Try to enable TOTP for specified user.
     * @param login users login
     * @return TOTP secret
     * @throws UserNotFoundException if user not found by login
     * @throws TOTPException TOTP already enabled for specified user
     */
    String enableTOTP(String login) throws UserNotFoundException, TOTPException;

    /**
     * Try to disable TOTP for specified user.
     * @param login users login
     * @throws UserNotFoundException if user not found by login
     * @throws TOTPException TOTP is not enabled for specified user
     */
    void disableTOTP(String login) throws UserNotFoundException, TOTPException;

    /**
     * Check user TOTP status.
     * @param login users login
     * @return true if TOTP for specified user is enabled, otherwise false
     */
    boolean isTOTPEnabled(String login);

    /**
     * Check user 2FA TOTP code.
     * @param login users login
     * @param totpCode TOTP code
     * @return true if TOTP code is valid, otherwise false
     * @throws UserNotFoundException user with specified login not found
     * @throws TOTPException TOTP is not enabled for specified user
     */
    boolean checkTOTPCode(String login, String totpCode) throws UserNotFoundException, TOTPException;

    /**
     * Update user auth date.
     * @param userId user ID
     */
    void refreshAuthDate(long userId) throws UserNotFoundException;

}
