package com.scindapsus.robot.config;

import com.scindapsus.robot.RobotMsgSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author wyh
 * @date 2022/10/9 15:07
 */
@Configuration
public class RobotAutoConfiguration {

    @Bean
    @ConditionalOnBean({RestTemplate.class})
    public RobotMsgSender robotMsgSender(ObjectProvider<JdbcTemplate> jdbcTemplate, RestTemplate restTemplate) {
        return new RobotMsgSender(jdbcTemplate.getIfAvailable(), restTemplate);
    }
}
