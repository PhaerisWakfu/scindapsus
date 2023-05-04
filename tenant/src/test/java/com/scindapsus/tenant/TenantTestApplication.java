package com.scindapsus.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author wyh
 * @since 2022/10/12
 */
@SpringBootApplication
public class TenantTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TenantProvider tenantProvider() {
        return request -> request.getHeader("company-id");
    }
}
