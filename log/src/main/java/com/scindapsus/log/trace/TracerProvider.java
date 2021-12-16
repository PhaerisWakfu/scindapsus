package com.scindapsus.log.trace;

/**
 * @author wyh
 * @since 1.0
 */
public interface TracerProvider {

    /**
     * 获取调用链ID
     *
     * @return 调用链ID
     */
    String getTraceId();
}
