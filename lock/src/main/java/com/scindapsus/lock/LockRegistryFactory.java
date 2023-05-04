package com.scindapsus.lock;

import org.springframework.integration.support.locks.LockRegistry;

/**
 * 琐注册工厂,根据琐类型会创建不同的工厂
 *
 * @author wyh
 * @since 2021/10/9
 */
@FunctionalInterface
public interface LockRegistryFactory {

    /**
     * 生成lock registry
     *
     * @return {@link LockRegistry}
     */
    LockRegistry generate();
}
