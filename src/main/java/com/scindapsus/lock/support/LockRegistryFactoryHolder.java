package com.scindapsus.lock.support;

import com.scindapsus.lock.LockRegistryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;


/**
 * 手动获取琐工具类
 *
 * @author wyh
 * @since 1.0
 */
public class LockRegistryFactoryHolder {

    private static LockRegistryFactory lockRegistryFactory;

    @Autowired
    public void setUp(LockRegistryFactory lockRegistryFactory) {
        LockRegistryFactoryHolder.lockRegistryFactory = lockRegistryFactory;
    }

    /**
     * 获取琐注册器
     *
     * @return 琐注册器
     */
    public static LockRegistry getLock() {
        return lockRegistryFactory.generate();
    }
}
