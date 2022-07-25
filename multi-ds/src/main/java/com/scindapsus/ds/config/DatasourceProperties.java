package com.scindapsus.ds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wyh
 * @date 2022/7/4 14:39
 */
@ConfigurationProperties(prefix = DatasourceProperties.PREFIX)
public class DatasourceProperties {

    public static final String PREFIX = "scindapsus.ds";

    /**
     * 多数据源配置
     */
    private Map<String, DSProperty> multi = new LinkedHashMap<>();



    public Map<String, DSProperty> getMulti() {
        return multi;
    }

    public void setMulti(Map<String, DSProperty> multi) {
        this.multi = multi;
    }

    public static class DSProperty {

        private Boolean isDefault = Boolean.FALSE;

        private Class<? extends DataSource> type;

        private String driverClassName;

        private String url;

        private String username;

        private String password;



        public Boolean getDefault() {
            return isDefault;
        }

        public void setDefault(Boolean aDefault) {
            isDefault = aDefault;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
