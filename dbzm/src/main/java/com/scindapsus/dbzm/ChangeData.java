package com.scindapsus.dbzm;

import io.debezium.data.Envelope;
import lombok.Data;

import java.util.Map;


@Data
public class ChangeData {

    /**
     * 数据源信息
     */
    private Source source;

    /**
     * 更改前数据
     */
    private Map<String, Object> after;

    /**
     * 更改后数据
     */
    private Map<String, Object> before;

    /**
     * 操作类型{@link Envelope.Operation}
     */
    private String op;

    /**
     * 事务相关
     */
    private Transaction transaction;


    /**
     * 获取操作类型枚举
     */
    public Envelope.Operation getOperation() {
        return Envelope.Operation.forCode(op);
    }

    @Data
    public static class Transaction {

        /**
         * 事务id
         */
        private String id;

        private Integer total_order;

        private Integer data_collection_order;
    }

    @Data
    public static class Source {

        /**
         * 源数据库的名称
         */
        private String db;

        /**
         * 源表的名称
         */
        private String table;

        /**
         * 源自的 binlog 或 log 文件的名称
         */
        private String file;

        /**
         * 连接器的类型（例如，"mysql"、"postgres"）
         */
        private String connector;

        /**
         * Debezium 连接器配置的名称
         */
        private String name;

        /**
         * 变更发生的 binlog 或 log 文件中的位置
         */
        private Long pos;

        /**
         * 变更在 binlog 文件中的行偏移位置
         */
        private Integer row;

        /**
         * 如果可用，全局事务标识符 (GTID)
         */
        private String gtid;

        /**
         * 生成事件的 MySQL 服务器的唯一标识
         */
        private String server_id;

        /**
         * Debezium 连接器的版本
         */
        private String version;

        /**
         * 事件发生的时间戳（毫秒）
         */
        private Long ts_ms;

        /**
         * 表示事件是否属于快照（"true" 或 "false")
         */
        private String snapshot;
    }
}