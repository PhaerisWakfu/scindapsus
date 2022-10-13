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

    public static Connection getConnection(String path) throws SQLException {
        Properties info = new Properties();
        info.put(MODEL_NAME, Sources.of(ResourceUtil.getResource(path)).file().getAbsolutePath());
        return DriverManager.getConnection(JDBC_PREFIX, info);
    }
}
