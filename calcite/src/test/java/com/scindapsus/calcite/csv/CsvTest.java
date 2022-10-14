package com.scindapsus.calcite.csv;

import com.scindapsus.calcite.ConnectionHelper;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class CsvTest {

    @Test
    public void select() throws SQLException {
        String sql = "SELECT u. NAME, u.age, c.class, p.phone FROM csv.userinfo u " +
                "INNER JOIN csv.class c ON u. NAME = c. NAME " +
                "INNER JOIN csv2.phone p ON u. NAME = p. NAME " +
                "WHERE u.age < 18";
        try (Connection connection = ConnectionHelper.getConnection("model.json")) {
            Statement statement = connection.createStatement();
            print(statement.executeQuery(sql));
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