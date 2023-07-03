package com.scindapsus.calcite;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.jdbc.Driver;
import org.apache.calcite.util.Sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author wyh
 * @since 2022/10/14
 */
public class ConnectionHelper {

    private static final String DEFAULT_LEX = "mysql";

    static {
        //解决非redis数据源下中文映射不对的问题
        System.setProperty("saffron.default.charset", "UTF-8");
    }

    public static Connection getConnection(String path) throws SQLException {
        return getConnectionByAbsolutePath(Sources.of(ResourceUtil.getResource(path)).file().getAbsolutePath());
    }

    public static Connection getConnectionByAbsolutePath(String absolutePath) throws SQLException {
        Properties info = new Properties();
        // 配置文件路径
        info.setProperty(CalciteConnectionProperty.MODEL.camelName(), absolutePath);
        // 设置SQL解析器
        info.setProperty(CalciteConnectionProperty.LEX.camelName(), DEFAULT_LEX);
        return DriverManager.getConnection(Driver.CONNECT_STRING_PREFIX, info);
    }
}
