package com.scindapsus.log.annotation;


import java.lang.annotation.*;

/**
 * 仅做需要打印请求日志的标记,见{@link com.scindapsus.log.aspect.LogAspect}
 *
 * @author wyh
 * @since 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OPLog {
}
