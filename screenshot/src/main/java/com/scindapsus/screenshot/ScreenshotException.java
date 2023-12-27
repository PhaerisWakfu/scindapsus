package com.scindapsus.screenshot;

/**
 * @author wyh
 * @since 2023/12/27
 */
public class ScreenshotException extends RuntimeException {

    public ScreenshotException() {
        super();
    }

    public ScreenshotException(Throwable e) {
        super(e);
    }

    public ScreenshotException(String message, Throwable e) {
        super(message, e);
    }
}
