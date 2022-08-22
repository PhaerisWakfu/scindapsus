package com.scindapsus.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 支持动态切换的数据源
 * 通过重写 determineCurrentLookupKey 实现数据源切换
 * <p>说明：动态切换库的事务是支持单库的</>
 *
 * @author Java课代表
 * @author wyh
 * @since 1.0
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 获取路由key,通过key可获取已设置数据源中对应的数据源
     * <p>如果lookupKey为空则获取默认数据源
     * <p>详见{@link AbstractRoutingDataSource#determineTargetDataSource}
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getRoutingKey();
    }
}