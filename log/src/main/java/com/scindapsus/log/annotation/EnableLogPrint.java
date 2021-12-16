package com.scindapsus.log.annotation;

import com.scindapsus.log.config.LogConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启AOP日志打印
 *
 * @author wyh
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LogConfiguration.class})
public @interface EnableLogPrint {
}
