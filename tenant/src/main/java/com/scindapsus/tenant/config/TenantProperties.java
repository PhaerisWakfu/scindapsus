package com.scindapsus.tenant.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wyh
 * @since 2022/10/12
 */
@Getter
@Setter
@ConfigurationProperties(prefix = TenantProperties.PREFIX)
public class TenantProperties {

    public static final String PREFIX = "scindapsus.tenant";

    /**
     * 租户key
     */
    private String name = "x-ss-tenant";

    /**
     * 是否将租户放到slf4j mdc中
     */
    private boolean mdc = true;

    /**
     * 租户设置与传播
     */
    @NestedConfigurationProperty
    private Propagation propagation = new Propagation();


    @Getter
    @Setter
    public static class Propagation {

        /**
         * 是否打开设置与传播租户开关
         */
        private boolean enabled = false;

        /**
         * 设置租户拦截路径
         */
        private String[] patterns = {"/**"};
    }
}
