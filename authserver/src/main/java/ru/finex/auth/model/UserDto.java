package ru.finex.auth.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author m0nster.mind
 */
@Data
public class UserDto {

    private final AtomicReference<AuthState> state = new AtomicReference<>();
    private String login;

    public UserDto(String login, AuthState state) {
        this.login = login;
        this.state.set(state);
    }
}
