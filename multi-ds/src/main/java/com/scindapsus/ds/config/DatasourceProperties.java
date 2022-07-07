package com.scindapsus.ds.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wyh
 * @date 2022/7/4 14:39
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DatasourceProperties.PREFIX)
public class DatasourceProperties {

    public static final String PREFIX = "scindapsus.ds";

    /**
     * 多数据源配置
     */
    private Map<String, DSProperty> multi = new LinkedHashMap<>();

    @Data
    public static class DSProperty {

        private Boolean isDefault = Boolean.FALSE;

        private Class<? extends DataSource> type;

        private String driverClassName;

        private String url;

        private String username;

        private String password;
    }
}
