package ru.finex.auth.model.exception;

/**
 * @author m0nster.mind
 */
public class UserAlreadyAuthorizedException extends RuntimeException {

    public UserAlreadyAuthorizedException() {
        super();
    }

    public UserAlreadyAuthorizedException(String message) {
        super(message);
    }

    public UserAlreadyAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyAuthorizedException(Throwable cause) {
        super(cause);
    }
}
