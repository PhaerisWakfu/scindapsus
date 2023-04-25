package com.scindapsus.calcite;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.calcite.util.Sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author wyh
 * @since 1.0
 */
public class ConnectionHelper {

    private static final String MODEL_NAME = "model";

    private static final String JDBC_PREFIX = "jdbc:calcite:";

    static {
        //解决非redis数据源下中文映射不对的问题
        System.setProperty("saffron.default.charset", "UTF-8");
    }

    public static Connection getConnection(String path) throws SQLException {
        return getConnectionByAbsolutePath(Sources.of(ResourceUtil.getResource(path)).file().getAbsolutePath());
    }

    public static Connection getConnectionByAbsolutePath(String absolutePath) throws SQLException {
        Properties info = new Properties();
        info.put(MODEL_NAME, absolutePath);
        return DriverManager.getConnection(JDBC_PREFIX, info);
    }
}
