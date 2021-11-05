package com.scindapsus.surl.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.scindapsus.surl.ShortUrl;
import com.scindapsus.surl.UrlMappingService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyh
 * @date 2021/11/4 18:46
 */
@Configuration
@EnableConfigurationProperties(ShortUrlProperties.class)
public class ShortUrlConfiguration {

    @Bean
    public DefaultShortUrlServiceImpl defaultShortUrlService() {
        return new DefaultShortUrlServiceImpl();
    }


    /**
     * 默认使用guava cache实现短链存取
     */
    public static class DefaultShortUrlServiceImpl implements UrlMappingService {

        private static final Cache<String, String> shortUrlMapping = CacheBuilder.newBuilder().build();

        @Override
        public void save(ShortUrl shortUrl) {
            shortUrlMapping.put(shortUrl.getKey(), shortUrl.getOriginalUrl());
        }

        @Override
        public String get(String key) {
            return shortUrlMapping.getIfPresent(key);
        }
    }

}
