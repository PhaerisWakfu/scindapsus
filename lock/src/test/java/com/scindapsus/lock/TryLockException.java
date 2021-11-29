package com.scindapsus.lock;

/**
 * @author wyh
 * @date 2021/11/24 11:24
 */
public class TryLockException extends RuntimeException {

    public TryLockException(String message, Throwable e) {
        super(message, e);
    }
}
