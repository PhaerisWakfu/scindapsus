package com.scindapsus.log;

import com.scindapsus.log.annotation.EnableLogPrint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wyh
 * @since 1.0
 */
@EnableLogPrint
@SpringBootApplication
public class LogApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
