package com.scindapsus.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成无需自定义方法的简单{@link org.springframework.data.jpa.repository.JpaRepository}
 *
 * @author wyh
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface FastJPA {

    /**
     * 生成路径
     *
     * @return SimpleRepository所在路径
     */
    String basePackage() default "";
}
