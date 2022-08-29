package com.scindapsus.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author wyh
 * @date 2022/8/29 15:04
 */
@Service
public class MyRobotServiceImpl extends AbstractRobotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JdbcTemplate setJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public RestTemplate setRestTemplate() {
        return restTemplate;
    }
}
