package com.scindapsus.ds.config;

import com.scindapsus.ds.RoutingDataSource;
import com.scindapsus.ds.constants.DSConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wyh
 * @date 2022/7/4 14:45
 */
@Configuration
public class DatasourceConfiguration {

    /**
     * routingDataSource也是dataSource
     * <p>这里指定该bean为主dataSource,防止多个datasource时导致mybatis的自动装配失效
     */
    @Bean
    @Primary
    public RoutingDataSource routingDataSource(Map<String, DataSource> dataSourceList) {
        //动态生成的数据源
        List<String> dynamicDatasourceList = dataSourceList.keySet().stream()
                .filter(x -> x.endsWith(DSConstants.DS_NAME_SUFFIX))
                .collect(Collectors.toList());
        //数据源路由器
        RoutingDataSource routingDataSource = new RoutingDataSource();
        //设置默认数据源
        routingDataSource.setDefaultTargetDataSource(
                dataSourceList.get(DSConstants.DEFAULT_ROUTING_KEY + DSConstants.DS_NAME_SUFFIX));
        //设置总共支持哪些数据源切换
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dynamicDatasourceList.forEach(x -> dataSourceMap.put(x, dataSourceList.get(x)));
        routingDataSource.setTargetDataSources(dataSourceMap);
        return routingDataSource;
    }
}
