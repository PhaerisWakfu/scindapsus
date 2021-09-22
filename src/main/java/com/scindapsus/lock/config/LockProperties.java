package com.scindapsus.lock.config;

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


    @Getter
    @Setter
    public static class Redis {

        /**
         * 锁前缀
         */
        private String registryKey = "scindapsus";

        /**
         * 默认过期时间(milliseconds)
         */
        private long expire = 60000;
    }

}
