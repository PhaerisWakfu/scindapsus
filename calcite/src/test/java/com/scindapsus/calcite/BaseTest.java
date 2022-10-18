package com.scindapsus.calcite;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author wyh
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CalciteTestApplication.class)
@AutoConfigureMockMvc
public abstract class BaseTest {

    @Autowired
    private MockMvc mockMvc;

    protected static void print(ResultSet resultSet) throws SQLException {
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
