package com.scindapsus.pulsar.exception;

/**
 * @author wyh
 * @since 2022/11/1
 */
public class PulsarConfigException extends RuntimeException {

    public PulsarConfigException(String message) {
        super(message);
    }

    public PulsarConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
