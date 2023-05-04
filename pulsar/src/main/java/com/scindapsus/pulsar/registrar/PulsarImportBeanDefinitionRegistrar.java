package com.scindapsus.pulsar.registrar;

import com.scindapsus.pulsar.config.PulsarProperties;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import com.scindapsus.pulsar.factory.PulsarConsumerFactoryBean;
import com.scindapsus.pulsar.factory.PulsarProducerFactoryBean;
import org.springframework.beans.factory.FactoryBean;
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
 * @since 2022/11/2
 */
public class PulsarImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private PulsarProperties properties;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        //注册消费者bean
        properties.getConsumer().forEach((name, config) -> registerFactoryBean(PulsarConsumerFactoryBean.class, registry, name, config));
        //注册生产者bean
        properties.getProducer().forEach((name, config) -> registerFactoryBean(PulsarProducerFactoryBean.class, registry, name, config));
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
     * @param factoryBeanClass bean的factoryBeanClass
     * @param registry         注册器
     * @param name             beanName
     * @param config           factoryBean构造bean所需的配置
     */
    private <T> void registerFactoryBean(Class<? extends FactoryBean<?>> factoryBeanClass, BeanDefinitionRegistry registry, String name, T config) {
        BeanDefinitionBuilder consumerBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(factoryBeanClass);
        consumerBuilder.addConstructorArgValue(config);
        BeanDefinition consumerDefinition = consumerBuilder.getBeanDefinition();
        registry.registerBeanDefinition(name, consumerDefinition);
    }
}
