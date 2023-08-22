package com.scindapsus.dbzm.config;

import com.scindapsus.dbzm.enums.ConnectorTypeEnum;
import com.scindapsus.dbzm.enums.OffsetBackingStoreEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wyh
 * @since 2023/8/21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = DebeziumProperties.PREFIX)
public class DebeziumProperties {

    public static final String PREFIX = "scindapsus.dbzm";

    private Map<String, DatasourceProperties> datasource = new HashMap<>();


    @Data
    public static class DatasourceProperties {

        /**
         * 快照模式（不同connector支持的mode不同）
         */
        private String snapshotMode;

        /**
         * 连接器类型
         */
        private ConnectorTypeEnum connectorType;

        /**
         * 服务id
         */
        private Integer serverId;

        /**
         * 服务名
         */
        private String serverName;

        /**
         * 被监听数据库host
         */
        private String hostname;

        /**
         * 被监听数据库port
         */
        private String port;

        /**
         * 被监听数据库用户名
         */
        private String user;

        /**
         * 被监听数据库用户密码
         */
        private String password;

        /**
         * 监控的数据库白名单, 如果选此值, 会忽略table.whitelist, 然后监控此db下所有表的binlog
         */
        private String databaseWhitelist;

        /**
         * 监控的表名白名单, 建议设置此值, 只监控这些表的binlog
         */
        private String tableWhitelist;

        /**
         * 保存offset的存储类型
         */
        private OffsetBackingStoreEnum offsetBackingStoreType = OffsetBackingStoreEnum.FILE;

        /**
         * 存放读取进度的本地文件地址
         */
        private String storageFile;

        /**
         * 历史文件路径
         */
        private String historyFile;

        /**
         * 读取进度刷新保存频率(ms), 默认1分钟.
         * 如果不依赖kafka的话, 应该就没有exactly once只读取一次语义, 应该是至少读取一次, 意味着可能重复读取.
         * 如果web容器挂了, 最新的读取进度没有刷新到文件里, 下次重启时, 就会重复读取binlog.
         */
        private Long flushInterval = 60000L;

        /**
         * 透传配置
         */
        private Map<String, String> directProperties;
    }
}
