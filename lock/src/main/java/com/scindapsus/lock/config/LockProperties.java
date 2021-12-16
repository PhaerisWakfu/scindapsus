package com.scindapsus.lock.config;

import com.scindapsus.lock.LockKeyPrefixGenerator;
import com.scindapsus.lock.enumeration.LockTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wyh
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = LockProperties.PREFIX)
public class LockProperties {

    public static final String PREFIX = "scindapsus.lock";

    /**
     * 琐类型
     */
    private LockTypeEnum type;

    /**
     * redis琐额外配置
     */
    @NestedConfigurationProperty
    private Redis redis = new Redis();

    /**
     * zk琐额外配置
     */
    @NestedConfigurationProperty
    private Zookeeper zookeeper = new Zookeeper();


    @Getter
    @Setter
    public static class Redis {

        /**
         * key的一级域,可配合{@link LockKeyPrefixGenerator}设置二级域
         */
        private String registryKey = "scindapsus";

        /**
         * 琐过期时间(milliseconds)
         */
        private long expire = 60000;
    }

    @Getter
    @Setter
    public static class Zookeeper {

        /**
         * zk连接
         */
        private String connectionString = "127.0.0.1:2181";

        /**
         * the path root (no trailing /).
         */
        private String root;

        /**
         * 连接重试之间等待的初始时间(milliseconds)
         */
        private int baseSleepTimeMs = 1000;

        /**
         * 连接最大重试次数
         */
        private int maxRetries = 3;
    }
}
