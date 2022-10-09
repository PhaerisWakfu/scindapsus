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

    /**
     * 必须注册，需要向机器人hook地址发送http请求
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 只需要发送固定简单消息（不带SQL）的可以不注册jdbcTemplate
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        String url = "jdbc:mysql://localhost:3306/ds1?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "root";
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        return JdbcUtil.jdbcTemplate(url, driverClassName, username, password);
    }
}
