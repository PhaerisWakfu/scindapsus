package com.scindapsus.ds.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * datasource factory bean
 *
 * @author wyh
 * @since 2022/7/4
 */
public class DataSourceFactoryBean implements FactoryBean<HikariDataSource> {

    private final HikariConfig properties;

    public DataSourceFactoryBean(HikariConfig properties) {
        this.properties = properties;
    }

    @Override
    public HikariDataSource getObject() {
        return new HikariDataSource(properties);
    }

    @Override
    public Class<HikariDataSource> getObjectType() {
        return HikariDataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
