package com.scindapsus.calcite;


import lombok.Data;

/**
 * @author wyh
 * @since 2023/4/24
 */
@Data
public class Schema {

    private String name;

    private File file;

    private Jdbc jdbc;

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
}
