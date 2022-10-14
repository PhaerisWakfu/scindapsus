# Scindapsus Calcite

> 利用ApacheCalcite通过SQL读取csv数据

## 使用

### 引入依赖
```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-calcite-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### 编写数据源配置文件

csvSchema 通过${directory}指定csv表路径

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

也可以使用calcite内置的mysql schema

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
    },
    {
      "name": "CSV2",
      "type": "custom",
      "factory": "com.scindapsus.calcite.csv.CsvSchemaFactory",
      "operand": {
        "directory": "csv2"
      }
    },
    {
      "name": "MSQ",
      "type": "custom",
      "factory": "org.apache.calcite.adapter.jdbc.JdbcSchema$Factory",
      "operand": {
        "jdbcDriver": "com.mysql.cj.jdbc.Driver",
        "jdbcUrl": "jdbc:mysql://localhost:3306/ds1",
        "jdbcUser": "root",
        "jdbcPassword": "root"
      }
    }
  ]
}
```

### 添加yml配置

```yaml
scindapsus:
  calcite:
    #是否使用自动装配注册数据源
    enabled: true
    #数据源配置文件所在路径
    config-path: model.json
```

### 添加配置类
```java
@Configuration
public class MyConfig {

    /**
     * 不想使用yml配置，也可以手动注册bean
     */
    @Bean
    public CalciteDatasource calciteDatasource() {
        return new CalciteDatasource("model.json");
    }

    /**
     * 不想通过静态工具类调用，可以注册方便操作SQL的ORM操作类
     */
    @Bean
    public JdbcTemplate jdbcTemplate(CalciteDatasource calciteDatasource) {
        return new JdbcTemplate(calciteDatasource);
    }
}
```

### 测试数据

#### schema[csv]

USERINFO
```
NAME:string,AGE:int
张三,18
李四,17
王五,10
赵六,20
```
CLASS
```
NAME:string,CLASS:string
张三,高一三班
李四,高二一班
王五,高一二班
赵六,高三一班
```
#### schema[csv2]

PHONE
```
NAME:string,PHONE:string
张三,110
李四,120
王五,119
赵六,114
```

#### schema[my]

```sql
CREATE TABLE `ADDRESS` (
  `ID` int NOT NULL,
  `NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名字',
  `AREA` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `idx_name` (`NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `ds1`.`ADDRESS` (`ID`, `NAME`, `AREA`) VALUES ('1', '张三', '上海');
INSERT INTO `ds1`.`ADDRESS` (`ID`, `NAME`, `AREA`) VALUES ('2', '李四', '北京');
INSERT INTO `ds1`.`ADDRESS` (`ID`, `NAME`, `AREA`) VALUES ('3', '王五', '上海');
INSERT INTO `ds1`.`ADDRESS` (`ID`, `NAME`, `AREA`) VALUES ('4', '赵六', '北京');
```

### 查询

#### 直接使用静态工具类
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

#### 注册bean使用orm操作类
```java
public class CsvTest extends BaseTest {

    /**
     * csv与mysql join
     */
    private static final String SQL = "SELECT u.name, u.age, c.class, p.phone, a.area FROM csv.userinfo u " +
            "INNER JOIN csv.class c ON u.name = c.name " +
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
}
```