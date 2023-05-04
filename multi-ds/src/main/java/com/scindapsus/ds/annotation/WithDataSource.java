package com.scindapsus.ds.annotation;

import java.lang.annotation.*;

/**
 * value如果是不存在的数据源则会走默认数据源
 *
 * @author Java课代表
 * @author wyh
 * @since 2022/7/4
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithDataSource {

    /**
     * 数据源名称,对应配置文件中的key
     */
    String value() default "";

    /**
     * 是否启用本地事务
     */
    boolean tx() default false;
}