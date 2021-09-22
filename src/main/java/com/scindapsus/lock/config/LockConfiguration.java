package com.scindapsus.lock.config;

import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.aspect.LockAspect;
import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.KeyPrefixGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyh
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
public class LockConfiguration {

    @Bean
    public LockAspect octopusLockAspect(LockRegistryFactory lockRegistryFactory) {
        return new LockAspect(lockRegistryFactory, new KeyPrefixGenerator());
    }

    @Bean
    @ConditionalOnMissingBean(LockRegistryFactory.class)
    public LockRegistryFactory defaultLockRegistryFactory() {
        return expire -> {
            throw new DistributedLockException("Please set an available lockRegistry");
        };
    }
}
