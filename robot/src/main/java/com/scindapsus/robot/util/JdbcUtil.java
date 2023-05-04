package com.scindapsus.robot.util;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author wyh
 * @since 2022/8/29
 */
public class JdbcUtil {

    private JdbcUtil() {
    }

    /**
     * 获取jdbcTemplate
     *
     * @param url             数据库连接
     * @param driverClassName 驱动名称
     * @param username        账号
     * @param password        密码
     * @return jdbcTemplate
     */
    public static JdbcTemplate jdbcTemplate(String url, String driverClassName, String username, String password) {
        return new JdbcTemplate(getDataSource(url, driverClassName, username, password));
    }

    /**
     * 获取数据源
     *
     * @param url             数据库连接
     * @param driverClassName 驱动名称
     * @param username        账号
     * @param password        密码
     * @return 数据源
     */
    public static DataSource getDataSource(String url, String driverClassName, String username, String password) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .type(HikariDataSource.class)
                .build();
    }
}
