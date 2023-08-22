package com.scindapsus.dbzm.registrar;

import com.scindapsus.dbzm.DebeziumConfigBuilder;
import com.scindapsus.dbzm.config.DebeziumProperties;
import io.debezium.config.Configuration;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author wyh
 * @since 2022/11/1
 */
public class ConfigurationFactoryBean implements FactoryBean<Configuration> {

    private final DebeziumProperties.DatasourceProperties properties;

    public ConfigurationFactoryBean(DebeziumProperties.DatasourceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Configuration getObject() throws Exception {
        return DebeziumConfigBuilder.build(properties);
    }

    @Override
    public Class<Configuration> getObjectType() {
        return Configuration.class;
    }
}
