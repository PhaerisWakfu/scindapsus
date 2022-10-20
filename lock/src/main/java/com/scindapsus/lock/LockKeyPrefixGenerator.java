package com.scindapsus.lock;


/**
 * 琐key前缀生成器,可自行实现自己的前缀生成器(如拼接租户等)
 *
 * @author wyh
 * @since 1.0
 */
@FunctionalInterface
public interface LockKeyPrefixGenerator {

    String SEPARATOR = ":";

    /**
     * 组装缓存key
     *
     * @param region 域
     * @param keys   键名
     * @return 组装后的琐名
     */
    String compute(String region, String... keys);
}
