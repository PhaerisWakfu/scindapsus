package com.scindapsus.calcite;


import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;
import java.util.Locale;

/**
 * @author wyh
 * @since 2023/4/24
 */
@Data
public class Schema {

    private String name;

    @NestedConfigurationProperty
    private File file;

    @NestedConfigurationProperty
    private Jdbc jdbc;

    @NestedConfigurationProperty
    private Redis redis;

    public String getName() {
        return name.toUpperCase(Locale.ROOT);
    }

    @Data
    public static class File {

        private String dir;
    }

    @Data
    public static class Jdbc {

        private String driver;

        private String url;

        private String user;

        private String password;
    }

    @Data
    public static class Redis {

        private String host;

        private String port;

        private String database;

        private String password;

        private List<RedisTable> tables;
    }

    @Data
    public static class RedisTable {

        private String name;

        private String dataFormat;

        private List<RedisTableField> fields;

        public String getName() {
            return name.toUpperCase(Locale.ROOT);
        }
    }

    @Data
    public static class RedisTableField {

        private String name;

        private String type;

        private String mapping;

        public String getName() {
            return name.toUpperCase(Locale.ROOT);
        }

        public String getMapping() {
            return mapping.toUpperCase(Locale.ROOT);
        }
    }
}
