package com.scindapsus.log.trace;

/**
 * @author wyh
 * @date 2021/10/9 11:17
 */
public interface TracerProvider {

    /**
     * 获取调用链ID
     *
     * @return 调用链ID
     */
    String getTraceId();
}
