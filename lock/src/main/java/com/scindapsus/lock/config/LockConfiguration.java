package com.scindapsus.lock.config;

import com.scindapsus.lock.LockFallback;
import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.aspect.LockAspect;
import com.scindapsus.lock.LockKeyPrefixGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.DefaultLockRegistry;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wyh
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
class LockConfiguration {

    @Bean
    LockAspect lockAspect(LockRegistryFactory lockRegistryFactory, LockKeyPrefixGenerator lockKeyPrefixGenerator,
                          ApplicationContext applicationContext) {
        return new LockAspect(lockRegistryFactory, lockKeyPrefixGenerator, applicationContext);
    }

    @Bean
    LockFallback.DefaultLockFallback defaultLockFallback() {
        return new LockFallback.DefaultLockFallback();
    }

    @Bean
    @ConditionalOnMissingBean(LockKeyPrefixGenerator.class)
    LockKeyPrefixGenerator defaultKeyPrefixGenerator() {
        return (region, keys) -> {
            StringBuilder keyAppender = new StringBuilder();
            Optional.ofNullable(region)
                    .ifPresent(keyAppender::append);
            Arrays.stream(keys).filter(StringUtils::isNotBlank)
                    .forEach(k -> keyAppender.append(LockKeyPrefixGenerator.SEPARATOR).append(k));
            return keyAppender.toString();
        };
    }

    @Bean
    @ConditionalOnMissingBean(LockRegistryFactory.class)
    LockRegistryFactory defaultLockRegistryFactory() {
        return DefaultLockRegistry::new;
    }
}
