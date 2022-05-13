package me.tomsdevsn.hetznercloud.exception;

/**
 * The Exception will be called, if an invalid parameters has been used.
 */
public class InvalidParametersException extends RuntimeException {

    private static final long serialVersionUID = 7465859521263546503L;

    public InvalidParametersException(String message) {
        super(message);
    }
}
