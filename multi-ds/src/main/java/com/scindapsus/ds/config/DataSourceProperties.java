package com.scindapsus.ds.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wyh
 * @since 2022/7/4
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {

    public static final String PREFIX = "scindapsus.ds";

    /**
     * 默认数据源
     */
    private String primary;

    /**
     * 多数据源配置
     */
    private Map<String, HikariConfig> multi = new LinkedHashMap<>();
}
