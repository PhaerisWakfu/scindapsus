package com.scindapsus.lock.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * distributed lock
 *
 * @author wyh
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * aliasFor {@link #key()}
     */
    @AliasFor("key")
    String value() default "";

    /**
     * region name
     */
    String name() default "";

    /**
     * lock key
     */
    String key() default "";

    /**
     * expire time(milliseconds)
     */
    long expire() default 60000;

    /**
     * retry duration(milliseconds)
     */
    long retryDuration() default -1;

    /**
     * locked callback, must provide a same signature method
     */
    Class<?> fallback() default void.class;
}
