# Scindapsus DS
根据<Java课代表>的文章《看透，Spring是如何支持多数据源的》(https://mp.weixin.qq.com/s/uR4FyEJKiU_75eGb4ynKiQ)
封装成多数据源组件



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
    multi:
      first:
        #是否是默认数据源
        is-default: true
        url: jdbc:mysql://101.200.128.253:3306/test-data?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8
        username: admin
        password: GLBFGop0Zh6KF@16E6
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
      second:
        url: jdbc:mysql://101.200.128.253:3306/test-data2?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8
        username: admin
        password: GLBFGop0Zh6KF@16E6
        driver-class-name: com.mysql.cj.jdbc.Driver
        type: com.zaxxer.hikari.HikariDataSource
```

## 使用

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