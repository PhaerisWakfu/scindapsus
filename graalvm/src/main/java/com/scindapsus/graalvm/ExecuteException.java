package com.scindapsus.graalvm;

/**
 * @author wyh
 * @since 1.0
 */
public class ExecuteException extends RuntimeException {

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
