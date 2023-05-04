package com.scindapsus.lock.config;

import com.scindapsus.lock.LockKeyPrefixGenerator;
import com.scindapsus.lock.enumeration.LockTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wyh
 * @since 2021/10/9
 */
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



    public LockTypeEnum getType() {
        return type;
    }

    public void setType(LockTypeEnum type) {
        this.type = type;
    }

    public Redis getRedis() {
        return redis;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public static class Redis {

        /**
         * key的一级域,可配合{@link LockKeyPrefixGenerator}设置二级域
         */
        private String registryKey = "scindapsus";

        /**
         * 琐过期时间(milliseconds)
         */
        private long expire = 60000;



        public String getRegistryKey() {
            return registryKey;
        }

        public void setRegistryKey(String registryKey) {
            this.registryKey = registryKey;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }
    }

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



        public String getConnectionString() {
            return connectionString;
        }

        public void setConnectionString(String connectionString) {
            this.connectionString = connectionString;
        }

        public String getRoot() {
            return root;
        }

        public void setRoot(String root) {
            this.root = root;
        }

        public int getBaseSleepTimeMs() {
            return baseSleepTimeMs;
        }

        public void setBaseSleepTimeMs(int baseSleepTimeMs) {
            this.baseSleepTimeMs = baseSleepTimeMs;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }
    }
}
