package com.scindapsus.surl.support;

import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import cn.hutool.core.lang.hash.MurmurHash;
import cn.hutool.core.util.RadixUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 短网址长转短工具类
 *
 * @author wyh
 * @date 2021/11/4 16:20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShortUrlHelper {

    /**
     * 生成短key存在后原始key拼接字符串
     */
    private static final String DUPLICATE_APPEND_KEY = "_duplicate";

    /**
     * 布隆过滤器
     */
    private static final BloomFilter BLOOM_FILTER = BloomFilterUtil.createBitMap(5);


    /**
     * 长转短
     *
     * @param originalStr 原始字符串
     * @return 短key
     */
    public static String long2Short(String originalStr) {
        //进行murmurHash并且转为59进制
        String radix59 = hashAndRadix59(originalStr);
        //使用布隆过滤器来减少hash冲突
        //如果布隆过滤器中不存在则肯定不存在
        if (!BLOOM_FILTER.contains(radix59)) {
            BLOOM_FILTER.add(radix59);
            return radix59;
        }
        //已存在拼接固定字符串
        originalStr += DUPLICATE_APPEND_KEY;
        return long2Short(originalStr);
    }

    /**
     * hash32并且转为59进制
     *
     * @param originalStr 原始字符串
     * @return 59进制字符串
     */
    private static String hashAndRadix59(String originalStr) {
        //转为murmurHash
        int hash32 = MurmurHash.hash32(originalStr);
        //负转正
        long hash32Tmp = (hash32 >= 0 ? hash32 : (0x100000000L - (~hash32 + 1)));
        //将hash值转为59进制降低长度
        return RadixUtil.encode(RadixUtil.RADIXS_59, hash32Tmp);
    }
}
