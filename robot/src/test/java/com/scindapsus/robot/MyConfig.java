package com.scindapsus.robot;

import com.scindapsus.robot.util.JdbcUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author wyh
 * @date 2022/8/29 15:08
 */
@Configuration
public class MyConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        String url = "jdbc:mysql://localhost:3306/ds1?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "root";
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        return JdbcUtil.jdbcTemplate(url, driverClassName, username, password);
    }
}
