package com.scindapsus.drools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author wyh
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DroolsProperties.PREFIX)
@Validated
public class DroolsProperties {

    public static final String PREFIX = "scindapsus.drools";

    /**
     * 规则所在路径
     */
    @NotBlank(message = "规则所在路径是必须的")
    private String rulesPath;
}
