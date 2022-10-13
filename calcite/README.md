# Scindapsus Calcite

> 利用ApacheCalcite通过SQL读取csv数据

## 使用

### 编写配置文件

通过${directory}指定csv表路径

```json
{
  "version": "1.0",
  "defaultSchema": "CSV",
  "schemas": [
    {
      "name": "CSV",
      "type": "custom",
      "factory": "com.scindapsus.calcite.csv.CsvSchemaFactory",
      "operand": {
        "directory": "csv"
      }
    }
  ]
}
```

### 测试表数据

```
NAME:string,AGE:int
张三,18
李四,17
王五,10
赵六,20
```

### 查询

```java
public class CsvTest {

    @Test
    public void select() throws SQLException {
        try (Connection connection = ConnectionHelper.getConnection("model.json")) {
            Statement statement = connection.createStatement();
            print(statement.executeQuery("select name,age from userinfo where age<18"));
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
```