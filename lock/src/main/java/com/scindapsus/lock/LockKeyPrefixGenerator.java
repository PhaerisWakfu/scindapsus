package com.scindapsus.lock;


/**
 * 锁key前缀生成器,可自行实现自己的前缀生成器(如拼接租户等)
 *
 * @author wyh
 * @since 2021/10/9
 */
@FunctionalInterface
public interface LockKeyPrefixGenerator {

    String SEPARATOR = ":";

    /**
     * 组装缓存key
     *
     * @param region 域
     * @param keys   键名
     * @return 组装后的锁名
     */
    String compute(String region, String... keys);
}
