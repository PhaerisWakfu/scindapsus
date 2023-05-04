package com.scindapsus.lock;

/**
 * @author wyh
 * @since 2021/10/9
 */
public class TryLockException extends RuntimeException {

    public TryLockException(String message, Throwable e) {
        super(message, e);
    }
}
