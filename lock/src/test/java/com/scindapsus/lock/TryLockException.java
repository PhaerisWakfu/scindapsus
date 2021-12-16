package com.scindapsus.lock;

/**
 * @author wyh
 * @since 1.0
 */
public class TryLockException extends RuntimeException {

    public TryLockException(String message, Throwable e) {
        super(message, e);
    }
}
