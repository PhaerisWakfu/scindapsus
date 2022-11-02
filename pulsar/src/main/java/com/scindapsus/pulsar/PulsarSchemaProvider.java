package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.Schema;

/**
 * @author wyh
 * @since 2022/11/1
 */
public interface PulsarSchemaProvider<T> {

    /**
     * 设置消息序列化类型
     *
     * @return {@link Schema}
     */
    Schema<T> get();
}
