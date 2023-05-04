package com.scindapsus.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wyh
 * @since 2022/8/29
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RobotTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RobotTestApplication.class, args);
    }
}
