package com.scindapsus.pulsar.factory;

import cn.hutool.core.bean.BeanUtil;
import com.scindapsus.pulsar.config.PulsarProperties;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import com.scindapsus.pulsar.tools.ClassUtil;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @author wyh
 * @since 2022/11/1
 */
public class PulsarProducerFactoryBean implements FactoryBean<Producer<?>> {

    @Autowired
    @SuppressWarnings("all")
    private PulsarClient client;

    private final PulsarProperties.Producer config;

    public PulsarProducerFactoryBean(PulsarProperties.Producer config) {
        this.config = config;
    }

    @Override
    public Producer<?> getObject() throws Exception {
        Schema<?> schema;
        try {
            schema = ClassUtil.getSchema(config.getSchemaClassName());
        } catch (Exception e) {
            throw new PulsarConfigException("schema class is not found");
        }
        ProducerBuilder<?> builder = client.newProducer(schema)
                .loadConf(BeanUtil.beanToMap(config.getProperty()));
        Optional.ofNullable(config.getCompressionType()).ifPresent(builder::compressionType);
        Optional.ofNullable(config.getAccessMode()).ifPresent(builder::accessMode);
        return builder.create();
    }

    @Override
    public Class<?> getObjectType() {
        return Producer.class;
    }
}
