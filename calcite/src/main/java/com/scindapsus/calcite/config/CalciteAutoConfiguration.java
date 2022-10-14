package com.scindapsus.calcite.config;

import com.scindapsus.calcite.CalciteDatasource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * @author wyh
 * @since 2022/10/14
 */
@Configuration
@EnableConfigurationProperties(CalciteProperties.class)
class CalciteAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CalciteDatasource.class)
    @ConditionalOnProperty(prefix = CalciteProperties.PREFIX, name = "enabled", havingValue = "true")
    CalciteDatasource calciteDatasource(CalciteProperties calciteProperties) {
        return new CalciteDatasource(Optional.ofNullable(calciteProperties.getConfigPath())
                .orElseThrow(() -> new IllegalArgumentException("config path must have a value")));
    }
}
