package com.scindapsus.calcite.csv;

import com.scindapsus.calcite.CalciteDatasource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author wyh
 * @since 2022/10/14
 */
@Configuration
public class MyConfig {

    /**
     * 不想使用yml配置，也可以手动注册bean
     */
    @Bean
    public CalciteDatasource calciteDatasource() {
        return new CalciteDatasource("model.json");
    }

    /**
     * 不想通过静态工具类调用，可以注册方便操作SQL的ORM操作类
     */
    @Bean
    public JdbcTemplate jdbcTemplate(CalciteDatasource calciteDatasource) {
        return new JdbcTemplate(calciteDatasource);
    }
}
