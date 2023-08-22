package com.scindapsus.dbzm.registrar;

import com.scindapsus.dbzm.config.DebeziumProperties;
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
public class ConfigurationImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private DebeziumProperties properties;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        properties.getDatasource().forEach((name, cfg) -> {
            BeanDefinitionBuilder consumerBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(ConfigurationFactoryBean.class);
            consumerBuilder.addConstructorArgValue(name);
            consumerBuilder.addConstructorArgValue(cfg);
            BeanDefinition consumerDefinition = consumerBuilder.getBeanDefinition();
            registry.registerBeanDefinition(name, consumerDefinition);
        });

    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        BindResult<DebeziumProperties> bind = Binder.get(environment)
                .bind(DebeziumProperties.PREFIX, DebeziumProperties.class);
        //配置校验这块强校验可优化该类由SPI注册改为@Import注解注册
        properties = bind.orElseThrow(() -> new IllegalArgumentException(String.format("Please config '%s'", DebeziumProperties.PREFIX)));
    }
}
