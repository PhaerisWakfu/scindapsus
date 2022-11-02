package com.scindapsus.pulsar.config;

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


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<String, Producer> getProducer() {
        return producer;
    }

    public void setProducer(Map<String, Producer> producer) {
        this.producer = producer;
    }

    public Map<String, Consumer> getConsumer() {
        return consumer;
    }

    public void setConsumer(Map<String, Consumer> consumer) {
        this.consumer = consumer;
    }


    /**
     * broker配置
     * <li>{@link Client#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     */
    public static class Client {

        private String name;

        private String authClassName;

        @NestedConfigurationProperty
        private ClientConfigurationData property;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthClassName() {
            return authClassName;
        }

        public void setAuthClassName(String authClassName) {
            this.authClassName = authClassName;
        }

        public ClientConfigurationData getProperty() {
            return property;
        }

        public void setProperty(ClientConfigurationData property) {
            this.property = property;
        }
    }

    /**
     * 生产者配置
     * <li>{@link Producer#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     */
    public static class Producer {

        private String schemaClassName;

        private CompressionType compressionType;

        private ProducerAccessMode accessMode;

        @NestedConfigurationProperty
        private ProducerConfigurationData property;


        public String getSchemaClassName() {
            return schemaClassName;
        }

        public void setSchemaClassName(String schemaClassName) {
            this.schemaClassName = schemaClassName;
        }

        public CompressionType getCompressionType() {
            return compressionType;
        }

        public void setCompressionType(CompressionType compressionType) {
            this.compressionType = compressionType;
        }

        public ProducerAccessMode getAccessMode() {
            return accessMode;
        }

        public void setAccessMode(ProducerAccessMode accessMode) {
            this.accessMode = accessMode;
        }

        public ProducerConfigurationData getProperty() {
            return property;
        }

        public void setProperty(ProducerConfigurationData property) {
            this.property = property;
        }
    }


    /**
     * 消费者配置
     * <li>{@link Consumer#property}中暂时只支持非对象配置
     * <li>枚举配置则直接放在了最外层
     * <li>对象类配置目前只支持了{@link ConsumerConfigurationData#messageListener}
     */
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

        public String getSchemaClassName() {
            return schemaClassName;
        }

        public void setSchemaClassName(String schemaClassName) {
            this.schemaClassName = schemaClassName;
        }

        public String getListenerClassName() {
            return listenerClassName;
        }

        public void setListenerClassName(String listenerClassName) {
            this.listenerClassName = listenerClassName;
        }

        public SubscriptionType getSubscriptionType() {
            return subscriptionType;
        }

        public void setSubscriptionType(SubscriptionType subscriptionType) {
            this.subscriptionType = subscriptionType;
        }

        public Map<String, String> getSubscriptionProperties() {
            return subscriptionProperties;
        }

        public void setSubscriptionProperties(Map<String, String> subscriptionProperties) {
            this.subscriptionProperties = subscriptionProperties;
        }

        public SubscriptionMode getSubscriptionMode() {
            return subscriptionMode;
        }

        public void setSubscriptionMode(SubscriptionMode subscriptionMode) {
            this.subscriptionMode = subscriptionMode;
        }

        public ConsumerCryptoFailureAction getCryptoFailureAction() {
            return cryptoFailureAction;
        }

        public void setCryptoFailureAction(ConsumerCryptoFailureAction cryptoFailureAction) {
            this.cryptoFailureAction = cryptoFailureAction;
        }

        public SubscriptionInitialPosition getSubscriptionInitialPosition() {
            return subscriptionInitialPosition;
        }

        public void setSubscriptionInitialPosition(SubscriptionInitialPosition subscriptionInitialPosition) {
            this.subscriptionInitialPosition = subscriptionInitialPosition;
        }

        public RegexSubscriptionMode getRegexSubscriptionMode() {
            return regexSubscriptionMode;
        }

        public void setRegexSubscriptionMode(RegexSubscriptionMode regexSubscriptionMode) {
            this.regexSubscriptionMode = regexSubscriptionMode;
        }

        public ConsumerConfigurationData<?> getProperty() {
            return property;
        }

        public void setProperty(ConsumerConfigurationData<?> property) {
            this.property = property;
        }
    }
}
