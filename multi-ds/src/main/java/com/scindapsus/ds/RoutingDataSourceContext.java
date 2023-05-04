package com.scindapsus.ds;

import com.scindapsus.ds.constants.DSConstants;
import org.springframework.util.StringUtils;

/**
 * @author Java课代表
 * @author wyh
 * @since 2022/7/4
 */
public class RoutingDataSourceContext {

    private static final ThreadLocal<String> LOOKUP_KEY_HOLDER = new ThreadLocal<>();

    public static void setRoutingKey(String routingKey) {
        LOOKUP_KEY_HOLDER.set(routingKey);
    }

    public static String getRoutingKey() {
        String name = LOOKUP_KEY_HOLDER.get();
        // 如果routingKey不存在则返回默认数据源
        return StringUtils.hasText(name) ? name + DSConstants.DS_NAME_SUFFIX : null;
    }

    public static void reset() {
        LOOKUP_KEY_HOLDER.remove();
    }
}