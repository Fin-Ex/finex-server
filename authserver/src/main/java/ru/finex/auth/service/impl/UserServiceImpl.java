package ru.finex.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.finex.auth.model.entity.UserEntity;
import ru.finex.auth.model.exception.DuplicateUserException;
import ru.finex.auth.model.exception.TOTPException;
import ru.finex.auth.model.exception.UserNotFoundException;
import ru.finex.auth.repository.UserRepository;
import ru.finex.auth.service.TOTPService;
import ru.finex.auth.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TOTPService totpService;

    @Override
    public UserEntity getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean checkPassword(String login, String password) {
        UserEntity user = getByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with '%s' login not found", login));
        }

        if (StringUtils.isBlank(user.getHash())) {
            return checkPassword(user, password);
        }

        return checkPassword(login, password, user.getHash());
    }

    @Override
    public boolean checkPassword(String login, String password, String hash) {
        UserEntity user = getByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with '%s' login not found", login));
        }

        if (!Objects.equals(user.getHash(), hash)) {
            return false;
        }

        String hashedPassword = hashPassword(password, hash);
        return checkPassword(user, hashedPassword);
    }

    private boolean checkPassword(UserEntity user, String password) {
        return Objects.equals(user.getPassword(), password);
    }

    private String hashPassword(String password, String hash) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return new String(Base64.getEncoder().encode(messageDigest.digest(password.getBytes())));
    }

    @Transactional
    @Override
    public void createUser(String login, String password, String hash) throws DuplicateUserException {
        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(hashPassword(password, hash));
        user.setHash(hash);
        userRepository.create(user);
    }

    @Override
    public void restorePassword(String login) throws UserNotFoundException {
        // TODO m0nster.mind: restore password logic:
        //  1. send a email/sms to user with check code - check code is saved to restore password codes
        //  2. wait to user enter code - check code between users input code and saved code into DB
        //  3. if codes is equals - generate random password for user and send it via email/sms
        //  4. delete saved DB code
        //  5. if DB code alive is more than 1 hour - delete it (via periodic task)
    }

    @Transactional
    @Override
    public String enableTOTP(String login) throws UserNotFoundException, TOTPException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User not found by login: " + login);
        }

        if (user.getSecret() != null) {
            throw new TOTPException(String.format("User '%s' already have secret", login));
        }

        String secret = totpService.generateSecret();
        userRepository.update(UserEntity.builder()
            .persistenceId(user.getPersistenceId())
            .secret(secret)
            .build()
        );
        totpService.generateRecoveryCodes(user.getPersistenceId());

        return secret;
    }

    @Transactional
    @Override
    public void disableTOTP(String login) throws UserNotFoundException, TOTPException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(String.format("User not found by login: %s", login));
        }

        if (user.getSecret() == null) {
            throw new TOTPException(String.format("User '%s' doesnt have a secret!", login));
        }

        userRepository.clearSecret(user.getPersistenceId());
        totpService.deleteRecoveryCodes(user.getPersistenceId());
    }

    @Override
    public boolean isTOTPEnabled(String login) {
        return userRepository.isSecretNotNull(login);
    }

    @Transactional(dontRollbackOn = { UserNotFoundException.class, TOTPException.class })
    @Override
    public boolean checkTOTPCode(String login, String totpCode) throws UserNotFoundException, TOTPException {
        UserEntity user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException(String.format("User not found by login: %s", login));
        }

        if (user.getSecret() == null) {
            throw new TOTPException(String.format("User '%s' doesnt have a secret!", login));
        }

        return totpService.verifyCode(totpCode, user.getSecret());
    }

    @Transactional
    @Override
    public void refreshAuthDate(long userId) throws UserNotFoundException {
        userRepository.updateAuthDate(userId);
    }
}
