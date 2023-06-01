package com.scindapsus.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;


/**
 * 手动获取琐
 *
 * @author wyh
 * @since 2021/10/9
 */
public class LockRegistryFactoryHolder {

    private static LockRegistryFactory lockRegistryFactory;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
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
