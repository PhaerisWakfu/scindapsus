package com.scindapsus.graalvm;

/**
 * @author wyh
 * @date 2022/7/25 10:55
 */
public class ExecuteException extends RuntimeException {

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
