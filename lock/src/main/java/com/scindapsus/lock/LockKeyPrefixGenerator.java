package com.scindapsus.lock;


/**
 * 琐key前缀生成器,可自行实现自己的前缀生成器(如拼接租户等)
 *
 * @author wyh
 * @date  2021/10/9 10:49
 */
@FunctionalInterface
public interface LockKeyPrefixGenerator {

    String SEPARATOR = ":";

    String EMPTY_STR = "";

    /**
     * 组装缓存key
     *
     * @param region 域
     * @param key    键名
     * @return 组装后的琐名
     */
    String compute(String region, String key);
}
