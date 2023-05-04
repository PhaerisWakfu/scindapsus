package com.scindapsus.surl.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scindapsus.surl.ShortUrl;
import com.scindapsus.surl.UrlMappingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author wyh
 * @since 2021/11/5
 */
@Configuration
@EnableConfigurationProperties(ShortUrlProperties.class)
class ShortUrlConfiguration {

    @Bean
    @ConditionalOnMissingBean(UrlMappingService.class)
    DefaultUrlMappingServiceImpl defaultUrlMappingService() {
        return new DefaultUrlMappingServiceImpl();
    }


    /**
     * 默认使用caffeine实现短链存取
     */
    public static class DefaultUrlMappingServiceImpl implements UrlMappingService {

        private static final Cache<String, String> shortUrlMapping =
                Caffeine.newBuilder()
                        //最后一次访问后1天过期
                        .expireAfterAccess(Duration.ofDays(1))
                        //弱引用key
                        .weakKeys()
                        //弱引用value
                        .weakValues()
                        .build();

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
