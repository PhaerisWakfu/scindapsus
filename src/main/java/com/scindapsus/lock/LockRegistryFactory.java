package com.scindapsus.lock;

import org.springframework.integration.support.locks.LockRegistry;

/**
 * 琐注册工厂,根据琐类型会创建不同的工厂
 *
 * @author wyh
 * @since 1.0
 */
@FunctionalInterface
public interface LockRegistryFactory {

    /**
     * 生成lock registry
     *
     * @param expire 过期时间，根据不同组件适配(milliseconds)
     * @return {@link LockRegistry}
     */
    LockRegistry generate(long expire);
}
