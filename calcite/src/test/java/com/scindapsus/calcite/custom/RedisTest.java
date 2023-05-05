package com.scindapsus.calcite.custom;

import com.scindapsus.calcite.BaseTest;
import com.scindapsus.calcite.CalciteDatasource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * redis支持并不完善，慎用<p>
 * 据官方文档<a href="https://calcite.apache.org/docs/redis_adapter.html">redis使用示例</a>所作，redis还存在不少问题：
 * <p>
 * <li>1.如果使用csv或者raw，则只支持简单的表结构</>
 * <li>2.如果使用json，则可以通过jsonArray的结构实现类似于数据局表的概念，但是查出的varchar类型都会带双引号，导致写SQL查询或者联表时会出问题</>
 *
 * @author wyh
 * @since 2022/10/18
 */
public class RedisTest extends BaseTest {

    private static final String SQL = "select name, deptno from redis.json where deptno=1";

    @Test
    public void select() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new CalciteDatasource("redis.json", false));
        System.out.println(jdbcTemplate.queryForList(SQL));
    }
}
