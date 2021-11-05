package com.scindapsus.surl;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 动态加载使用urlMappingSpi的静态工具
 *
 * @author wyh
 * @date 2021/11/4 16:20
 */
public class UrlMappingHolder {

    private static UrlMappingService urlMappingService;

    @Autowired
    public void setUp(UrlMappingService urlMappingService) {
        UrlMappingHolder.urlMappingService = urlMappingService;
    }

    /**
     * 保存原始URL与短key的关系
     *
     * @param originalUrl 原始URL
     * @param requestUrl  映射的url(e.g. http://localhost:8080)
     * @return 生成的短网址
     */
    public static String mapping(String originalUrl, String requestUrl) {
        return urlMappingService.mapping(originalUrl, requestUrl);
    }

    /**
     * 根据短key查找原始URL
     *
     * @param key 短key
     * @return 原始URL
     */
    public static String find(String key) {
        return urlMappingService.find(key);
    }
}
