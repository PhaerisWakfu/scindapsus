package com.scindapsus.calcite.csv;

import com.scindapsus.calcite.ConnectionHelper;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class CsvTest {

    @Test
    public void select() throws SQLException {
        try (Connection connection = ConnectionHelper.getConnection("model.json")) {
            Statement statement = connection.createStatement();
            print(statement.executeQuery("select u.name,u.age,c.class from userinfo u inner join class c on u.name=c.name where u.age<18"));
        }
    }

    private static void print(ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; ; i++) {
                System.out.print(resultSet.getString(i));
                if (i < columnCount) {
                    System.out.print(", ");
                } else {
                    System.out.println();
                    break;
                }
            }
        }
    }
}