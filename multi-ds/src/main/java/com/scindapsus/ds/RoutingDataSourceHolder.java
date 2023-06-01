package com.scindapsus.ds;

import com.scindapsus.ds.constants.DSConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * @author wyh
 * @since 2022/7/4
 */
public class RoutingDataSourceHolder {

    private static RoutingDataSource routingDataSource;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    public void setUp(RoutingDataSource routingDataSource) {
        RoutingDataSourceHolder.routingDataSource = routingDataSource;
    }

    /**
     * 根据名称获取数据源
     *
     * @param name 数据源配置中的key
     * @return 数据源
     */
    public static DataSource get(String name) {
        return lookup(name + DSConstants.DS_NAME_SUFFIX);

    }

    /**
     * 获取当前线程的数据源
     *
     * @return 数据源
     */
    public static DataSource current() {
        return lookup(routingDataSource.determineCurrentLookupKey());
    }

    /**
     * 根据名称获取数据源
     *
     * @param dataSourceName 数据源名称
     * @return 数据源
     */
    private static DataSource lookup(Object dataSourceName) {
        DataSource dataSource = routingDataSource.getResolvedDataSources().get(dataSourceName);
        return dataSource != null ? dataSource : routingDataSource.getResolvedDefaultDataSource();
    }
}
