package com.scindapsus.ds.annotation;

import com.scindapsus.ds.config.DataSourceImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解开启多数据源功能
 *
 * @author wyh
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DataSourceImportBeanDefinitionRegistrar.class)
public @interface EnableDS {
}
