package com.scindapsus.ds;

import com.scindapsus.ds.annotation.EnableDS;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wyh
 * @date 2022/7/5 10:18
 */
@MapperScan(basePackages = "com.scindapsus.ds.mapper")
@SpringBootApplication
@EnableDS
@EnableTransactionManagement
public class DSApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSApplication.class, args);
    }
}
