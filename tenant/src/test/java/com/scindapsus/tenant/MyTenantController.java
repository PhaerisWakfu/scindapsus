package com.scindapsus.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author wyh
 * @since 1.0
 */
@RestController
@RequestMapping({"/tenant", "/company"})
public class MyTenantController {

    private final Logger log = LoggerFactory.getLogger(MyTenantController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test1")
    public String test1() {
        log.info("服务8080开始调用服务8081");
        return restTemplate.getForObject("http://localhost:8081/company/test2", String.class);
    }

    @GetMapping("/test2")
    public String test2() {
        log.info("服务8081被调用了");
        return Optional.ofNullable(TenantHolder.getTenant()).orElse("未获取到租户");
    }
}
