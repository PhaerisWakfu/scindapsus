package com.scindapsus.ds.config;

import com.scindapsus.ds.RoutingDataSource;
import com.scindapsus.ds.aspect.DataSourceAspect;
import com.scindapsus.ds.constants.DSConstants;
import com.scindapsus.ds.exception.DataSourceException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wyh
 * @date 2022/7/4 14:45
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfiguration {

    @Bean
    public DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }

    /**
     * routingDataSource也是dataSource
     * <p>这里指定该bean为主dataSource,防止多个datasource时导致mybatis的自动装配失效
     */
    @Bean
    @Primary
    public RoutingDataSource routingDataSource(Map<String, DataSource> dataSourceMap, DataSourceProperties datasourceProperties) {
        //第一个配置的数据源为默认数据源
        String defaultDataSourceKey = datasourceProperties.getMulti()
                .keySet()
                .stream().findFirst()
                .orElseThrow(() -> new DataSourceException("Please set @EnableDS for your application"));
        //如果手动指定了默认数据源
        String primaryKey = datasourceProperties.getPrimary();
        if (StringUtils.hasText(primaryKey)
                && dataSourceMap.containsKey(primaryKey + DSConstants.DS_NAME_SUFFIX)) {
            defaultDataSourceKey = primaryKey;
        }
        //数据源路由器
        RoutingDataSource routingDataSource = new RoutingDataSource(defaultDataSourceKey);
        //设置默认数据源
        routingDataSource.setDefaultTargetDataSource(dataSourceMap.get(defaultDataSourceKey + DSConstants.DS_NAME_SUFFIX));
        //设置总共支持哪些数据源切换
        routingDataSource.setTargetDataSources(dataSourceMap.keySet().stream()
                .filter(x -> x.endsWith(DSConstants.DS_NAME_SUFFIX))
                .collect(Collectors.toMap(x -> x, dataSourceMap::get)));
        return routingDataSource;
    }

}
