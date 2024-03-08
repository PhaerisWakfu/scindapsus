# Scindapsus Calcite

> 无需懂calcite语法，简单配置通过SQL读取异构数据

## 使用

### 引入依赖
```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-calcite-spring-boot-starter</artifactId>
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

### 直接使用可添加yml配置

```yaml
scindapsus:
  calcite:
    schemas:
      - name: json
        file:
          dir: json
      - name: csv
        file:
          dir: csv
      - name: my
        jdbc:
          driver: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/ds1
          user: root
          password: root
      - name: redis
        redis:
          host: localhost
          port: 6379
          database: 0
          password:
          tables:
            - name: json
              data-format: json
              fields:
                - name: deptno
                  type: varchar
                  mapping: deptno
                - name: name
                  type: varchar
                  mapping: name
```
### 也可自行使用calcite语法创建数据源

#### csv
file类型通过${directory}指定表路径

```json
{
  "version": "1.0",
  "defaultSchema": "CSV",
  "schemas": [
    {
      "name": "CSV",
      "type": "custom",
      "factory": "org.apache.calcite.adapter.file.FileSchemaFactory",
      "operand": {
        "directory": "csv"
      }
    }
  ]
}
```
#### json
file类型通过${directory}指定表路径

```json
{
  "version": "1.0",
  "defaultSchema": "JSON",
  "schemas": [
    {
      "name": "JSON",
      "type": "custom",
      "factory": "org.apache.calcite.adapter.file.FileSchemaFactory",
      "operand": {
        "directory": "json"
      }
    }
  ]
}
```

#### mysql

```json
{
  "version": "1.0",
  "defaultSchema": "MY",
  "schemas": [
    {
      "name": "MY",
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

#### redis

```json
{
  "version": "1.0",
  "defaultSchema": "REDIS",
  "schemas": [
    {
      "type": "custom",
      "name": "REDIS",
      "factory": "org.apache.calcite.adapter.redis.RedisSchemaFactory",
      "operand": {
        "host": "localhost",
        "port": 6379,
        "database": 0,
        "password": ""
      },
      "tables": [
        {
          "name": "JSON",
          "factory": "org.apache.calcite.adapter.redis.RedisTableFactory",
          "operand": {
            "dataFormat": "json",
            "fields": [
              {
                "name": "DEPTNO",
                "type": "varchar",
                "mapping": "DEPTNO"
              },
              {
                "name": "NAME",
                "type": "varchar",
                "mapping": "NAME"
              }
            ]
          }
        }
      ]
    }
  ]
}
```
#### 自行添加配置读取自定义的数据源配置文件
```java
@Configuration
public class MyConfig {

    @Bean
    public CalciteDatasource calciteDatasource() {
        return new CalciteDatasource("mix.json", false);
    }
}
```

### 测试数据

#### csv数据

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
#### json数据

PHONE
```json
[
  {
    "NAME": "张三",
    "PHONE": "110"
  },
  {
    "NAME": "李四",
    "PHONE": "120"
  },
  {
    "NAME": "王五",
    "PHONE": "119"
  },
  {
    "NAME": "赵六",
    "PHONE": "114"
  }
]
```

#### mysql数据

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

#### redis数据
```shell
redis-cli LPUSH JSON '{"DEPTNO":1,"NAME":"张三"}'
redis-cli LPUSH JSON '{"DEPTNO":2,"NAME":"李四"}'
redis-cli LPUSH JSON '{"DEPTNO":3,"NAME":"王五"}'
redis-cli LPUSH JSON '{"DEPTNO":4,"NAME":"赵六"}'
```

### 查询

#### 注册bean使用orm操作类
```java
public class MixTest extends BaseTest {

    private static final String SQL = "SELECT u.name, u.age, c.class, p.phone, a.area, r.deptno FROM csv.userinfo u " +
            //自己在windows创建的csv文件记得要修改字符集格式为UTF-8
            "INNER JOIN csv.class c ON u.name = c.name " +
            //数据文件中的key注意大写
            "INNER JOIN json.phone p ON u.name = p.name " +
            //这里是mysql的表，注意，添加mysql schema的时候，库中的表名与字段一定要大写，不然无法识别
            "INNER JOIN my.address a ON u.name = a.name " +
            //这是redis的表
            "INNER JOIN redis.json r on u.name = r.name " +
            "WHERE u.age = 18";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void selectByJdbcTemplate() {
        System.out.println(jdbcTemplate.queryForList(SQL).toString());
    }
}
```