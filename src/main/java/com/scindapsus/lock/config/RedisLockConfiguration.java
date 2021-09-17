package com.scindapsus.lock.config;

import com.scindapsus.lock.LockRegistryFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author wyh
 * @since 1.0
 */
@Configuration
@ConditionalOnClass({RedisConnectionFactory.class, RedisLockRegistry.class})
@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "redis")
public class RedisLockConfiguration {

    @Bean
    public RedisLockRegistryFactory redisLockRegistry(LockProperties lockProperties,
                                                      RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistryFactory(lockProperties, redisConnectionFactory);
    }


    public static class RedisLockRegistryFactory implements LockRegistryFactory<RedisLockRegistry> {

        private final LockProperties properties;

        private final RedisConnectionFactory connectionFactory;

        public RedisLockRegistryFactory(LockProperties properties, RedisConnectionFactory connectionFactory) {
            this.properties = properties;
            this.connectionFactory = connectionFactory;
        }

        @Override
        public RedisLockRegistry generate(long expire) {
            return new RedisLockRegistry(connectionFactory, properties.getRegistryKey(), expire > 0 ? expire : properties.getExpire());
        }
    }
}
