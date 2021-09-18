package com.scindapsus.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;


/**
 * @author wyh
 * @date 2021/1/7 17:56
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
     * @param expire expire 过期时间，根据不同注册器适配(milliseconds)
     * @return 琐注册器
     */
    public static LockRegistry getLock(long expire) {
        return lockRegistryFactory.generate(expire);
    }
}
