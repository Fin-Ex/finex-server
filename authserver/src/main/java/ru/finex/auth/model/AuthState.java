package ru.finex.auth.model;

/**
 * @author m0nster.mind
 */
public enum AuthState {

    NONE,
    CHECK_PASSWORD,
    CHECK_2FA,
    AUTHED

}
