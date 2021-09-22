package com.scindapsus.lock.annotation;

import com.scindapsus.lock.LockFallback;
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
    long expire() default -1;

    /**
     * retry duration(milliseconds)
     */
    long retryDuration() default -1;

    /**
     * locked callback, must implements {@link LockFallback}
     */
    Class<? extends LockFallback> fallback() default LockFallback.DefaultLockFallback.class;
}
