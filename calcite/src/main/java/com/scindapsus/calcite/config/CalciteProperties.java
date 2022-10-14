package com.scindapsus.calcite.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wyh
 * @since 2022/10/14
 */
@ConfigurationProperties(prefix = CalciteProperties.PREFIX)
public class CalciteProperties {

    public static final String PREFIX = "scindapsus.calcite";

    /**
     * 自动注册datasource bean开关
     */
    private boolean enabled = false;

    /**
     * 配置文件路径
     */
    private String configPath;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}
