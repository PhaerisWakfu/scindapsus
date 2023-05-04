package com.scindapsus.ds;

import com.scindapsus.ds.tx.ConnectionFactory;
import com.scindapsus.ds.tx.ConnectionProxy;
import com.scindapsus.ds.tx.TxUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 支持动态切换的数据源
 * 通过重写 determineCurrentLookupKey 实现数据源切换
 *
 * @author Java课代表
 * @author wyh
 * @since 2022/7/4
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final String defaultDataSourceName;

    public RoutingDataSource(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }

    /**
     * 获取路由key,通过key可获取已设置数据源中对应的数据源
     * <p>如果lookupKey为空则获取默认数据源
     * <p>详见{@link AbstractRoutingDataSource#determineTargetDataSource}
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceContext.getRoutingKey();
    }

    @Override
    @SuppressWarnings("all")
    public Connection getConnection() throws SQLException {
        if (TxUtil.getTxId() == null) {
            return super.getConnection();
        }
        return getTxConnection();
    }

    @Override
    @SuppressWarnings("all")
    public Connection getConnection(String username, String password) throws SQLException {
        if (TxUtil.getTxId() == null) {
            return super.getConnection(username, password);
        }
        return getTxConnection();
    }

    /**
     * 获取代理的connection{@link ConnectionProxy}
     */
    private Connection getTxConnection() throws SQLException {
        //获取当前数据源名称
        String name = (String) determineCurrentLookupKey();
        //如果没有则拿默认数据源名称
        name = name != null ? name : defaultDataSourceName;
        //看看当前数据源的连接在不在连接工厂里
        ConnectionProxy connection = ConnectionFactory.getConnection(name);
        //不在放到连接工厂中，在则返回
        return connection == null
                ? ConnectionFactory.putConnection(name, new ConnectionProxy(name, determineTargetDataSource().getConnection()))
                : connection;
    }
}