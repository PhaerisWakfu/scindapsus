package com.scindapsus.tenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author wyh
 * @since 1.0
 */
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMdc() {
        return mdc;
    }

    public void setMdc(boolean mdc) {
        this.mdc = mdc;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }


    public static class Propagation {

        /**
         * 是否打开设置与传播租户开关
         */
        private boolean enabled = false;

        /**
         * 设置租户拦截路径
         */
        private String[] patterns = {"/**"};


        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getPatterns() {
            return patterns;
        }

        public void setPatterns(String[] patterns) {
            this.patterns = patterns;
        }
    }
}
