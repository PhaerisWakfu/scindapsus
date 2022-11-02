package com.scindapsus.pulsar;

import com.scindapsus.pulsar.annotation.EnabledPulsar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wyh
 * @since 2022/11/1
 */
@EnabledPulsar
@SpringBootApplication
public class PulsarTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PulsarTestApplication.class, args);
    }
}
