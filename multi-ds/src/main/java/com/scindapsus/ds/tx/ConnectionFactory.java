package com.scindapsus.ds.tx;

import com.scindapsus.ds.exception.DataSourceException;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 改造于baomidou的多数据源本地事务方案<a href="https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter">dynamic-datasource</a>
 *
 * @author funkye
 * @author wyh
 * @since 2022/7/4
 */
public class ConnectionFactory {

    private static final ThreadLocal<Map<String, ConnectionProxy>> CONNECTION_HOLDER =
            ThreadLocal.withInitial(() -> new ConcurrentHashMap<>(8));


    /**
     * 设置当前线程所有的数据源名以及其对应的connection
     *
     * @param name       数据源名称
     * @param connection 数据库连接
     */
    public static ConnectionProxy putConnection(String name, ConnectionProxy connection) {
        Map<String, ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
        if (!concurrentHashMap.containsKey(name)) {
            try {
                connection.setAutoCommit(false);
                concurrentHashMap.put(name, connection);
            } catch (SQLException e) {
                throw new DataSourceException("设置自动提交事务失败");
            }
        }
        return connection;
    }

    /**
     * 获取当前线程的指定数据源的connection
     *
     * @param name 数据源名称
     * @return connection
     */
    public static ConnectionProxy getConnection(String name) {
        return CONNECTION_HOLDER.get().get(name);
    }

    /**
     * 通知当前线程的所有的connection一起commit或者rollback
     *
     * @param state true commit /false rollback
     */
    public static void notify(boolean state) throws Exception {
        Exception exception = null;
        try {
            Map<String, ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
            for (ConnectionProxy connectionProxy : concurrentHashMap.values()) {
                try {
                    connectionProxy.notify(state);
                } catch (SQLException e) {
                    exception = e;
                }
            }
        } finally {
            CONNECTION_HOLDER.remove();
            if (exception != null) {
                throw exception;
            }
        }
    }

}
