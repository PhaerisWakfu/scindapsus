package com.scindapsus.dbzm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wyh
 * @since 2023/8/22
 */
@AllArgsConstructor
@Getter
public enum ConnectorTypeEnum {

    MYSQL("io.debezium.connector.mysql.MySqlConnector"),

    POSTGRES("io.debezium.connector.postgresql.PostgresConnector"),

    MONGODB("io.debezium.connector.mongodb.MongoDbConnector"),

    ORACLE("io.debezium.connector.oracle.OracleConnector"),

    SQLSERVER("io.debezium.connector.sqlserver.SqlServerConnector");

    private final String connectorClassName;
}
