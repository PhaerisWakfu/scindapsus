package com.scindapsus.dbzm;

import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * @author wyh
 * @since 2023/8/21
 */
public interface CDCEvent {

    /**
     * 监听事件(at-least-once)
     *
     * @param destination 来源（${connectorName}.${schema}.${table}）
     * @param key         主键
     * @param value       变更数据
     */
    void onMessage(String destination, @Nullable Map<String, Object> key, ChangeData value);
}
