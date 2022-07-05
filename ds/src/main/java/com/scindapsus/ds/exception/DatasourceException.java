package com.scindapsus.ds.exception;

/**
 * @author wyh
 * @since 1.0
 */
public class DatasourceException extends RuntimeException {

    public DatasourceException(String message) {
        super(message);
    }

    public DatasourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatasourceException(Throwable cause) {
        super(cause);
    }
}
