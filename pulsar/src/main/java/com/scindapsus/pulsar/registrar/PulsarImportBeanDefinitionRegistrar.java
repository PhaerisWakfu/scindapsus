package com.scindapsus.pulsar.registrar;

import com.scindapsus.pulsar.config.PulsarProperties;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import com.scindapsus.pulsar.factory.PulsarConsumerFactoryBean;
import com.scindapsus.pulsar.factory.PulsarProducerFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author wyh
 * @since 1.0
 */
public class PulsarImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private PulsarProperties properties;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        registerBean(properties, registry);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        BindResult<PulsarProperties> bind = Binder.get(environment)
                .bind(PulsarProperties.PREFIX, PulsarProperties.class);
        properties = bind.orElseThrow(() -> new PulsarConfigException(String.format("Please config '%s'", PulsarProperties.PREFIX)));
    }

    /**
     * 注册bean
     *
     * @param config   broker配置
     * @param registry 注册器
     */
    public void registerBean(PulsarProperties config, BeanDefinitionRegistry registry) {
        //注册消费者bean
        config.getConsumer().forEach((k, v) -> {
            BeanDefinitionBuilder consumerBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(PulsarConsumerFactoryBean.class);
            consumerBuilder.addConstructorArgValue(v);
            BeanDefinition consumerDefinition = consumerBuilder.getBeanDefinition();
            registry.registerBeanDefinition(k, consumerDefinition);
        });
        //注册生产者bean
        config.getProducer().forEach((k, v) -> {
            BeanDefinitionBuilder producerBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(PulsarProducerFactoryBean.class);
            producerBuilder.addConstructorArgValue(v);
            BeanDefinition producerDefinition = producerBuilder.getBeanDefinition();
            registry.registerBeanDefinition(k, producerDefinition);
        });
    }
}
