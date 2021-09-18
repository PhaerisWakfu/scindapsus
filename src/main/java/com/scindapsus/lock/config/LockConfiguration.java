package com.scindapsus.lock.config;

import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.aspect.LockAspect;
import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.KeyPrefixGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

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

    @Configuration
    @ConditionalOnClass({RedisConnectionFactory.class, RedisLockRegistry.class})
    @ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "redis")
    public static class RedisLockConfiguration {

        @Bean
        public LockRegistryFactory redisLockRegistry(LockProperties lockProperties,
                                                     RedisConnectionFactory redisConnectionFactory) {
            return new RedisLockConfiguration.RedisLockRegistryFactory(lockProperties, redisConnectionFactory);
        }


        public static class RedisLockRegistryFactory implements LockRegistryFactory {

            private final LockProperties properties;

            private final RedisConnectionFactory connectionFactory;

            public RedisLockRegistryFactory(LockProperties properties, RedisConnectionFactory connectionFactory) {
                this.properties = properties;
                this.connectionFactory = connectionFactory;
            }

            @Override
            public LockRegistry generate(long expire) {
                return new RedisLockRegistry(connectionFactory, properties.getRegistryKey(), expire > 0 ? expire : properties.getExpire());
            }
        }
    }
}
