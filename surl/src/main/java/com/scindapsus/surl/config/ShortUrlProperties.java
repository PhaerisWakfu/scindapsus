package com.scindapsus.surl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wyh
 * @date 2021/11/4 17:41
 */
@Getter
@Setter
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
}
