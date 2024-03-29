package com.scindapsus.calcite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CalciteTest extends BaseTest {

    private static final String SQL = "SELECT u.name, u.age, c.class, p.phone, a.area FROM csv.userinfo u " +
            //自己在windows创建的csv文件记得要修改字符集格式为UTF-8
            "INNER JOIN csv.class c ON u.name = c.name " +
            //数据文件中的key注意大写
            "INNER JOIN json.phone p ON u.name = p.name " +
            //这里是mysql的表，注意，添加mysql schema的时候，库中的表名与字段一定要大写，不然无法识别
            "INNER JOIN my.address a ON u.name = a.name " +
            "WHERE u.name='张三'";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void selectByJdbcTemplate() {
        System.out.println(jdbcTemplate.queryForList(SQL).toString());
    }
}