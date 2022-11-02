package com.scindapsus.pulsar.annotation;

import com.scindapsus.pulsar.registrar.PulsarImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wyh
 * @since 2022/11/1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PulsarImportBeanDefinitionRegistrar.class)
public @interface EnablePulsar {
}
