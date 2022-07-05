package com.scindapsus.ds;

import com.scindapsus.ds.constants.DSConstants;

/**
 * @author Java课代表
 * @author wyh
 * @since 1.0
 */
public class RoutingDataSourceContext {

    private static final ThreadLocal<String> LOOKUP_KEY_HOLDER = new ThreadLocal<>();

    public static void setRoutingKey(String routingKey) {
        LOOKUP_KEY_HOLDER.set(routingKey);
    }

    public static String getRoutingKey() {
        String name = LOOKUP_KEY_HOLDER.get();
        // 如果key不存在则返回默认数据源
        String key = name == null ? DSConstants.DEFAULT_ROUTING_KEY : name;
        return key + DSConstants.DS_NAME_SUFFIX;
    }

    public static void reset() {
        LOOKUP_KEY_HOLDER.remove();
    }
}