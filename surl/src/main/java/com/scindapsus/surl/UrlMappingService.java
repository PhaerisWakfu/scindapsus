package com.scindapsus.surl;

import com.scindapsus.surl.exception.UnknownMappingException;
import com.scindapsus.surl.support.ShortUrlHelper;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * @author wyh
 * @date 2021/11/4 17:58
 */
public interface UrlMappingService {

    String HTTP_PREFIX = "http://";


    /**
     * 如何保存短链key与原始URL的关系
     *
     * @param shortUrl 短链key与原始URL的关系
     */
    void save(ShortUrl shortUrl);

    /**
     * 如何根据短key查找原始URL
     *
     * @param key 短key
     * @return 原始URL
     */
    String get(String key);

    /**
     * 根据原始URL生成短链
     *
     * @param originalUrl 原始URL
     * @param requestUrl  映射的url(e.g. http://localhost:8080)
     * @return 生成的短网址
     */
    default String mapping(String originalUrl, String requestUrl) {
        if (!StringUtils.hasText(originalUrl)) {
            return null;
        }
        //如果没有前缀拼上前缀
        originalUrl = originalUrl.startsWith(HTTP_PREFIX) ? originalUrl : HTTP_PREFIX + originalUrl;
        //获取短链key
        String key = ShortUrlHelper.long2Short(originalUrl);
        //持久化
        save(ShortUrl.builder()
                .originalUrl(originalUrl)
                .key(key).build());
        //生成短网址
        return String.format("%s/%s", requestUrl, key);
    }

    /**
     * 根据短key查找原始URL
     *
     * @param key 短key
     * @return 原始URL
     */
    default String find(String key) {
        return Optional.ofNullable(get(key)).orElseThrow(() -> new UnknownMappingException(key));
    }
}
