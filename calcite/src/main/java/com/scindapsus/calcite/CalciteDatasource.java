package com.scindapsus.calcite;

import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author wyh
 * @since 2022/10/14
 */
public class CalciteDatasource extends AbstractDataSource {

    /**
     * 配置文件路径
     * <p>
     * e.g config/my.json
     */
    private final String configPath;

    public CalciteDatasource(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ConnectionHelper.getConnection(configPath);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }
}
