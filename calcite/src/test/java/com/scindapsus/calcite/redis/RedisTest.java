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

    /**
     * LPUSH json {"DEPTNO":1,"NAME":"张三1"}
     * LPUSH json {"DEPTNO":2,"NAME":"张三2"}
     */
    private static final String SQL = "select * from redis.json where deptno=2";

    @Test
    public void select() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new CalciteDatasource("redis.json"));
        System.out.println(jdbcTemplate.queryForList(SQL));
    }
}
