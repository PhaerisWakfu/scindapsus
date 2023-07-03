package com.scindapsus.pulsar.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.conf.ClientConfigurationData;
import org.apache.pulsar.client.impl.conf.ConsumerConfigurationData;
import org.apache.pulsar.client.impl.conf.ProducerConfigurationData;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * @author wyh
 * @since 2022/11/1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = PulsarProperties.PREFIX)
public class PulsarProperties {

    public static final String PREFIX = "scindapsus.pulsar";

    /**
     * 客户端
     */
    @NestedConfigurationProperty
    private Client client = new Client();

    /**
     * 生产者
     */
    private Map<String, Producer> producer;

    /**
     * 消费者
     */
    private Map<String, Consumer> consumer;


    /**
     * broker配置
     * <li>{@link Client#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     */
    @Getter
    @Setter
    public static class Client {

        private String name;

        private String authClassName;

        @NestedConfigurationProperty
        private ClientConfigurationData property;
    }

    /**
     * 生产者配置
     * <li>{@link Producer#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     */
    @Getter
    @Setter
    public static class Producer {

        private String schemaClassName;

        private CompressionType compressionType;

        private ProducerAccessMode accessMode;

        @NestedConfigurationProperty
        private ProducerConfigurationData property;
    }


    /**
     * 消费者配置
     * <li>{@link Consumer#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     * <li>对象类配置目前只支持了{@link ConsumerConfigurationData#messageListener}
     */
    @Getter
    @Setter
    public static class Consumer {

        private String schemaClassName;

        private String listenerClassName;

        private SubscriptionType subscriptionType;

        private Map<String, String> subscriptionProperties;

        private SubscriptionMode subscriptionMode;

        private ConsumerCryptoFailureAction cryptoFailureAction;

        private SubscriptionInitialPosition subscriptionInitialPosition;

        private RegexSubscriptionMode regexSubscriptionMode;

        @NestedConfigurationProperty
        private ConsumerConfigurationData<?> property;
    }
}
