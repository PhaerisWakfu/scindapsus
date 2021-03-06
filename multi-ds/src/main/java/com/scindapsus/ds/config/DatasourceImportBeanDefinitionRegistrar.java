package com.scindapsus.ds.config;

import com.scindapsus.ds.constants.DSConstants;
import com.scindapsus.ds.exception.DatasourceException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Optional;

/**
 * @author wyh
 * @date 2022/7/4 15:06
 */
public class DatasourceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private DatasourceProperties datasourceProperties;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, DatasourceProperties.DSProperty> datasourcePropertiesMap = datasourceProperties.getMulti();
        datasourcePropertiesMap.forEach((k, v) -> registerBean(k, v, registry));
        //额外注册一个默认的数据源
        Optional<DatasourceProperties.DSProperty> defaultDS = datasourcePropertiesMap.values().stream().filter(DatasourceProperties.DSProperty::getIsDefault).findFirst();
        if (!defaultDS.isPresent()) {
            throw new DatasourceException("Must contain a default datasource");
        }
        DatasourceProperties.DSProperty dsProperty = defaultDS.get();
        registerBean(DSConstants.DEFAULT_ROUTING_KEY, dsProperty, registry);
    }

    @Override
    public void setEnvironment(Environment environment) {
        datasourceProperties = Binder.get(environment).bind(DatasourceProperties.PREFIX, DatasourceProperties.class).get();
    }

    /**
     * 注册bean
     *
     * @param name     数据源名
     * @param property 数据源配置
     * @param registry 注册器
     */
    public static void registerBean(String name, DatasourceProperties.DSProperty property, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class);
        builder.addConstructorArgValue(property);
        BeanDefinition definition = builder.getBeanDefinition();
        String beanName = name + DSConstants.DS_NAME_SUFFIX;
        registry.registerBeanDefinition(beanName, definition);
    }
}
