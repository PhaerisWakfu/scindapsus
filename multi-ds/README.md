# Scindapsus DS

根据<Java课代表>的文章[《看透，Spring是如何支持多数据源的》]
封装成多数据源组件

## 说明

不支持mybatis-plus，其官方有自己的多数据源包[Dynamic-Datasource]，推荐使用官方的，功能更强大，支持更友好

## 依赖

```xml
<!--快照版本-->
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-ds-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
        <!--根据自己需要引入orm依赖与驱动包ds-->
<dependency>
<groupId>org.mybatis.spring.boot</groupId>
<artifactId>mybatis-spring-boot-starter</artifactId>
<version>2.2.2</version>
<scope>test</scope>
</dependency>
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<scope>test</scope>
</dependency>
```

## 配置

```yaml
scindapsus:
  ds:
    #默认数据源（不写默认为第一个配置的数据源）
    primary: first
    multi:
      first:
        jdbc-url: jdbc:mysql://localhost:3306/ds1?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8
        username: root
        password: root
        driver-class-name: com.mysql.cj.jdbc.Driver
      second:
        jdbc-url: jdbc:mysql://localhost:3306/ds2?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8
        username: root
        password: root
        driver-class-name: com.mysql.cj.jdbc.Driver
        #使用hikariCP来作数据库连接池，支持hikariCP的配置
        connection-test-query: SELECT 1
        connection-timeout: 60000
        idle-timeout: 500000
        max-lifetime: 540000
        maximum-pool-size: 20
        minimum-idle: 10
```

## 使用

启动类添加启动多数据源的注解@EnableDS

```java
/**
 * @author wyh
 * @date 2022/7/5 10:18
 */
@MapperScan(basePackages = "com.scindapsus.ds.mapper")
@SpringBootApplication
@EnableDS
public class DSApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSApplication.class, args);
    }
}
```

在操作库的地方使用注解@WithDataSource指定数据源，不加注解或者不写value默认为isDefault为true的数据源(写多个isDefault默认取第一个)

```java
/**
 * @author wyh
 * @date 2022/7/5 15:34
 */
@Service
@AllArgsConstructor
public class CarService {

    private final CarMapper carMapper;

    public Car firstGet() {
        return carMapper.getOne(1L);
    }

    /**
     * 指定数据源key
     */
    @WithDataSource("second")
    public Car secondGet() {
        return carMapper.getOne(1L);
    }
}
```

[《看透，Spring是如何支持多数据源的》]:https://mp.weixin.qq.com/s/at-QJjpFi3PK7jyk0hCwcA

[Dynamic-Datasource]:https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter