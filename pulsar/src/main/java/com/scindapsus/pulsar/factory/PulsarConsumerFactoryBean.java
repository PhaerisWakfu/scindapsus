package com.scindapsus.pulsar.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.scindapsus.pulsar.config.PulsarProperties;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import com.scindapsus.pulsar.tools.ClassUtil;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @author wyh
 * @since 2022/11/1
 */
public class PulsarConsumerFactoryBean implements SmartFactoryBean<Consumer<?>> {

    @Autowired
    @SuppressWarnings("all")
    private PulsarClient client;

    private final PulsarProperties.Consumer config;

    public PulsarConsumerFactoryBean(PulsarProperties.Consumer config) {
        this.config = config;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Consumer<?> getObject() throws Exception {
        Schema<?> schema;
        MessageListener listener;
        try {
            schema = ClassUtil.getSchema(config.getSchemaClassName());
            listener = ReflectUtil.newInstance(config.getListenerClassName());
        } catch (Exception e) {
            throw new PulsarConfigException("schema or listener class is not found");
        }
        ConsumerBuilder<?> builder = client.newConsumer(schema)
                .loadConf(BeanUtil.beanToMap(config.getProperty()))
                .messageListener(listener);
        Optional.ofNullable(config.getSubscriptionType()).ifPresent(builder::subscriptionType);
        Optional.ofNullable(config.getSubscriptionMode()).ifPresent(builder::subscriptionMode);
        Optional.ofNullable(config.getRegexSubscriptionMode()).ifPresent(builder::subscriptionTopicsMode);
        Optional.ofNullable(config.getSubscriptionInitialPosition()).ifPresent(builder::subscriptionInitialPosition);
        Optional.ofNullable(config.getSubscriptionProperties()).ifPresent(builder::subscriptionProperties);
        Optional.ofNullable(config.getCryptoFailureAction()).ifPresent(builder::cryptoFailureAction);
        return builder.subscribe();
    }

    @Override
    public Class<?> getObjectType() {
        return Consumer.class;
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }
}
