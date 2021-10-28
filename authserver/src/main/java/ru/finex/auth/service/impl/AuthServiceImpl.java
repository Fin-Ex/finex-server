package ru.finex.auth.service.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.auth.model.AuthState;
import ru.finex.auth.model.UserDto;
import ru.finex.auth.model.exception.TOTPException;
import ru.finex.auth.model.exception.UserNotFoundException;
import ru.finex.auth.service.AuthService;
import ru.finex.auth.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class AuthServiceImpl implements AuthService {

    private final Map<String, UserDto> users = new ConcurrentHashMap<>();
    private final UserService userService;

    @Transactional
    @Override
    public AuthState authUser(String login, String password) {
        UserDto user = startUserAuth(login);
        if (!user.getState().compareAndSet(AuthState.NONE, AuthState.CHECK_PASSWORD)) {
            throw new RuntimeException();
        }

        if (!checkUserPassword(login, password, user)) {
            return AuthState.NONE;
        }

        return nextAuthState(login, user);
    }

    @Transactional
    @Override
    public AuthState authUserTOTP(String login, String totpCode) {
        UserDto user = users.get(login);
        if (user == null) {
            return AuthState.NONE;
        }

        boolean result;
        try {
            result = userService.checkTOTPCode(login, totpCode);
        } catch (UserNotFoundException | TOTPException e) {
            result = false;
        }

        if (!result || !user.getState().compareAndSet(AuthState.CHECK_2FA, AuthState.AUTHED)) {
            failUserAuth(login, user);
        }

        return AuthState.AUTHED;
    }

    private UserDto startUserAuth(String login) {
        UserDto user = users.get(login);
        if (user != null) {
            return user;
        }

        return users.computeIfAbsent(login, e -> new UserDto(login, AuthState.NONE));
    }

    private boolean checkUserPassword(String login, String password, UserDto user) {
        boolean result = false;
        try {
            result = userService.checkPassword(login, password);
        } catch (UserNotFoundException e) {
            // result already is false
        }

        if (!result) {
            failUserAuth(login, user);
        }

        return result;
    }

    private AuthState nextAuthState(String login, UserDto user) {
        AuthState nextState;
        if (userService.isTOTPEnabled(login)) {
            nextState = AuthState.CHECK_2FA;
        } else {
            nextState = AuthState.AUTHED;
        }

        if (!user.getState().compareAndSet(AuthState.CHECK_PASSWORD, nextState)) {
            failUserAuth(login, user);
            return AuthState.NONE;
        }

        return nextState;
    }

    private void failUserAuth(String login, UserDto user) {
        users.remove(login, user);
    }

}
