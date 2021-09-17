package com.scindapsus.lock;

import com.scindapsus.lock.exception.DistributedLockException;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * @author wyh
 * @since 1.0
 */
public interface LockRegistryFactory<T extends LockRegistry> {

    /**
     * 生成lock registry
     *
     * @param expire 过期时间，根据不同组件适配(milliseconds)
     * @return {@link LockRegistry}
     */
    T generate(long expire) throws DistributedLockException;
}
