package com.scindapsus.surl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wyh
 * @since 1.0
 */
@ConfigurationProperties(prefix = ShortUrlProperties.PREFIX)
public class ShortUrlProperties {

    public static final String PREFIX = "scindapsus.surl";

    /**
     * 是否开启自带的接口
     */
    private boolean enabled;

    /**
     * 自带接口的path
     */
    private String path;



    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
