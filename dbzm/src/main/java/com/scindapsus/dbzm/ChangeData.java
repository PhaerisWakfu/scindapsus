package com.scindapsus.dbzm;

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
     * 操作类型{@link io.debezium.data.Envelope.Operation}
     */
    private String op;

    @Data
    public static class Source {

        private String db;

        private String table;

        private String file;

        private String connector;

        private String name;

        private Long pos;

        private Integer row;

        private String gtid;

        private String server_id;

        private String version;

        private Long ts_ms;

        private String snapshot;
    }
}