package com.scindapsus.ds;

import com.scindapsus.ds.annotation.EnableDS;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wyh
 * @since 1.0
 */
@MapperScan(basePackages = "com.scindapsus.ds.mapper")
@SpringBootApplication
@EnableDS
public class DSTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSTestApplication.class, args);
    }
}
