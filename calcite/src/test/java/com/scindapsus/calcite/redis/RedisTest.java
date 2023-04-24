package com.scindapsus.calcite.redis;

import com.scindapsus.calcite.BaseTest;
import com.scindapsus.calcite.CalciteDatasource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * @author wyh
 * @since 2022/10/18
 */
public class RedisTest extends BaseTest {

    private static final String SQL = "select * from redis.json where deptno=2";

    @Test
    public void select() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new CalciteDatasource("redis.json", false));
        System.out.println(jdbcTemplate.queryForList(SQL));
    }
}
