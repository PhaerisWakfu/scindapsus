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

    /**
     * 是否是绝对路径
     */
    private final boolean absolute;

    public CalciteDatasource(String configPath, boolean absolute) {
        this.configPath = configPath;
        this.absolute = absolute;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return absolute ? ConnectionHelper.getConnectionByAbsolutePath(configPath) : ConnectionHelper.getConnection(configPath);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }
}
