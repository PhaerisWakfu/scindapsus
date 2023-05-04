package com.scindapsus.lock.config;

import com.scindapsus.lock.LockRegistryFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.integration.zookeeper.config.CuratorFrameworkFactoryBean;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

import java.util.Optional;

/**
 * @author wyh
 * @since 2021/10/9
 */
@ConditionalOnClass({CuratorFramework.class, ZookeeperLockRegistry.class})
@ConditionalOnProperty(prefix = LockProperties.PREFIX, name = "type", havingValue = "zookeeper")
@AutoConfigureBefore(LockConfiguration.class)
@Configuration
public class ZookeeperLockConfiguration {

    @Bean
    public CuratorFrameworkFactoryBean curatorFrameworkFactoryBean(LockProperties properties) {
        return new CuratorFrameworkFactoryBean(
                properties.getZookeeper().getConnectionString(),
                new ExponentialBackoffRetry(properties.getZookeeper().getBaseSleepTimeMs(),
                        properties.getZookeeper().getMaxRetries())
        );
    }

    @Bean
    public ZookeeperLockRegistryFactory zookeeperLockRegistryFactory(LockProperties properties, CuratorFramework curatorFramework) {
        return new ZookeeperLockRegistryFactory(properties, curatorFramework);
    }


    public static class ZookeeperLockRegistryFactory implements LockRegistryFactory {

        private final LockProperties properties;

        private final CuratorFramework curatorFramework;

        public ZookeeperLockRegistryFactory(LockProperties properties, CuratorFramework curatorFramework) {
            this.properties = properties;
            this.curatorFramework = curatorFramework;
        }

        @Override
        public LockRegistry generate() {
            return Optional.ofNullable(properties.getZookeeper().getRoot())
                    .map(root -> new ZookeeperLockRegistry(curatorFramework, root))
                    .orElse(new ZookeeperLockRegistry(curatorFramework));
        }
    }
}
