package com.scindapsus.lock.annotation;

import com.scindapsus.lock.LockFallback;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * distributed lock
 *
 * @author wyh
 * @since 2021/10/9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * aliasFor {@link #key()}
     */
    @AliasFor("key")
    String[] value() default {};

    /**
     * region name
     */
    String name() default "";

    /**
     * lock key
     */
    String[] key() default {};

    /**
     * retry duration(milliseconds)
     */
    long retryDuration() default -1;

    /**
     * locked fallback, must implements {@link LockFallback}
     */
    Class<? extends LockFallback<?>> fallback() default LockFallback.DefaultLockFallback.class;
}
