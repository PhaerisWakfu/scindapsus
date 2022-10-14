package com.scindapsus.calcite.csv;

import com.scindapsus.calcite.ConnectionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;

public class CsvTest extends BaseTest {

    /**
     * csv与mysql join
     */
    private static final String SQL = "SELECT u.name, u.age, c.class, p.phone, a.area FROM csv.userinfo u " +
            "INNER JOIN csv.class c ON u.name = c.name " +
            //自己在windows创建的csv文件记得要修改字符集格式为UTF-8
            "INNER JOIN csv2.phone p ON u.name = p.name " +
            //这里是mysql的表，注意，添加mysql schema的时候，库中的表名与字段一定要大写，不然无法识别
            "INNER JOIN my.address a ON u.name = a.name " +
            "WHERE u.age < 18";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void selectByJdbcTemplate() {
        System.out.println(jdbcTemplate.queryForList(SQL).toString());
    }

    @Test
    public void select() throws SQLException {
        try (Connection connection = ConnectionHelper.getConnection("model.json")) {
            Statement statement = connection.createStatement();
            print(statement.executeQuery(SQL));
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