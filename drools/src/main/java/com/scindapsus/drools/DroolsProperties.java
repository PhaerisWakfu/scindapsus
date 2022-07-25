package com.scindapsus.drools;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author wyh
 * @since 1.0
 */
@ConfigurationProperties(prefix = DroolsProperties.PREFIX)
@Validated
public class DroolsProperties {

    public static final String PREFIX = "scindapsus.drools";

    /**
     * 规则所在路径
     */
    @NotBlank(message = "规则所在路径是必须的")
    private String rulesPath;

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }
}
