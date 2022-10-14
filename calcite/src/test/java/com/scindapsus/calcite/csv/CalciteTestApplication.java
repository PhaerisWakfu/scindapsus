package com.scindapsus.calcite.csv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wyh
 * @since 2022/10/14
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CalciteTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalciteTestApplication.class, args);
    }
}
