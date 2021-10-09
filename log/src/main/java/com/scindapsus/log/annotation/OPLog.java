package com.scindapsus.log.annotation;


import java.lang.annotation.*;

/**
 * 日志标记
 *
 * @author wyh
 * @date 2021/10/9 10:49
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OPLog {
}
