package com.scindapsus.ds.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wyh
 * @since 1.0
 */
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


    public Map<String, HikariConfig> getMulti() {
        return multi;
    }

    public void setMulti(Map<String, HikariConfig> multi) {
        this.multi = multi;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }
}
