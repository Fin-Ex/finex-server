package ru.finex.auth.model.exception;

/**
 * @author m0nster.mind
 */
public class TOTPException extends RuntimeException {

    public TOTPException() {
        super();
    }

    public TOTPException(String message) {
        super(message);
    }

    public TOTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public TOTPException(Throwable cause) {
        super(cause);
    }

}
