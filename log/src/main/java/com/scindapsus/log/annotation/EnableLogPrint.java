package com.scindapsus.log.annotation;

import com.scindapsus.log.config.LogConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启AOP日志打印
 *
 * @author wyh
 * @date 2021/11/19 15:31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LogConfiguration.class})
public @interface EnableLogPrint {
}
