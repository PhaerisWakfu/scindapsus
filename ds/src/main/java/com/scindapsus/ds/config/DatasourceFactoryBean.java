package com.scindapsus.ds.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * datasource factory bean
 *
 * @author wyh
 * @since 1.0
 */
public class DatasourceFactoryBean implements FactoryBean<DataSource> {

    private final DatasourceProperties.DSProperty properties;

    public DatasourceFactoryBean(DatasourceProperties.DSProperty properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getObject() {
        return DataSourceBuilder.create()
                .type(properties.getType())
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }

    @Override
    public Class<? extends DataSource> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
