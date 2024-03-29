package com.scindapsus.lock.config;

import com.scindapsus.lock.LockRegistryFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * redis锁的实现
 *
 * @author wyh
 * @since 2021/10/9
 */
@ConditionalOnClass({RedisConnectionFactory.class, RedisLockRegistry.class})
@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "redis")
@AutoConfigureBefore(LockConfiguration.class)
@Configuration
public class RedisLockConfiguration {

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
        public LockRegistry generate() {
            return new RedisLockRegistry(connectionFactory, properties.getRedis().getRegistryKey(),
                    properties.getRedis().getExpire());
        }
    }
}