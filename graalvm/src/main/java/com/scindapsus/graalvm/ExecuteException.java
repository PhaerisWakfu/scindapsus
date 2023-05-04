package com.scindapsus.graalvm;

/**
 * @author wyh
 * @since 2022/7/25
 */
public class ExecuteException extends RuntimeException {

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
