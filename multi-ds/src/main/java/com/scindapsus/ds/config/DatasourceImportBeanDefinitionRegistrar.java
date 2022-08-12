package com.scindapsus.ds.config;

import com.scindapsus.ds.constants.DSConstants;
import com.scindapsus.ds.exception.DatasourceException;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author wyh
 * @date 2022/7/4 15:06
 */
public class DatasourceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private DatasourceProperties datasourceProperties;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, HikariConfig> datasourcePropertiesMap = datasourceProperties.getMulti();
        datasourcePropertiesMap.forEach((k, v) -> registerBean(k, v, registry));
    }

    @Override
    public void setEnvironment(Environment environment) {
        BindResult<DatasourceProperties> bind = Binder.get(environment)
                .bind(DatasourceProperties.PREFIX, DatasourceProperties.class);
        //如果没有配置数据源
        datasourceProperties = bind.orElseThrow(() -> new DatasourceException(String.format("Please config '%s'", DatasourceProperties.PREFIX)));
        if (datasourceProperties.getMulti().isEmpty()) {
            throw new DatasourceException("Please provide configuration for at least one datasource");
        }
    }

    /**
     * 注册bean
     *
     * @param name     数据源名
     * @param property 数据源配置
     * @param registry 注册器
     */
    public static void registerBean(String name, HikariConfig property, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class);
        builder.addConstructorArgValue(property);
        BeanDefinition definition = builder.getBeanDefinition();
        String beanName = name + DSConstants.DS_NAME_SUFFIX;
        registry.registerBeanDefinition(beanName, definition);
    }
}
