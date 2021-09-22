package com.scindapsus.lock.config;

import com.scindapsus.lock.LockFallback;
import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.aspect.LockAspect;
import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.KeyPrefixGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author wyh
 * @since 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LockProperties.class)
public class LockConfiguration {

    @Bean
    public LockAspect octopusLockAspect(LockRegistryFactory lockRegistryFactory, KeyPrefixGenerator keyPrefixGenerator,
                                        ApplicationContext applicationContext) {
        return new LockAspect(lockRegistryFactory, keyPrefixGenerator, applicationContext);
    }

    @Bean
    public LockFallback.DefaultLockFallback defaultLockFallback() {
        return new LockFallback.DefaultLockFallback();
    }

    @Bean
    @ConditionalOnMissingBean(KeyPrefixGenerator.class)
    public KeyPrefixGenerator defaultKeyPrefixGenerator() {
        return (region, key) -> {
            String regionName = Optional.ofNullable(region).map(x -> x + KeyPrefixGenerator.SEPARATOR)
                    .orElse(KeyPrefixGenerator.EMPTY_STR);
            return regionName + key;
        };
    }

    @Bean
    @ConditionalOnMissingBean(LockRegistryFactory.class)
    public LockRegistryFactory defaultLockRegistryFactory() {
        return expire -> {
            throw new DistributedLockException("Please set an available lockRegistry");
        };
    }
}
