package com.scindapsus.ds.annotation;

import java.lang.annotation.*;

/**
 * @author Java课代表
 * @since 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithDataSource {

    String value() default "";
}