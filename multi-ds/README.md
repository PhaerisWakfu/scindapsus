# Scindapsus DS

根据<Java课代表>的文章[《看透，Spring是如何支持多数据源的》]
封装成多数据源组件，并且支持多数据源事务（参考baomidou团队[Dynamic-Datasource]的本地事务解决方案）

## 说明

- 不支持mybatis-plus，其官方有自己的多数据源包[Dynamic-Datasource]，推荐使用官方的，功能更强大，支持更友好
- 如果使用spring声明式事务@Transactional，仅支持单数据源事务（推荐使用本地事务解决多数据源事务问题）

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

在操作库的地方使用注解@WithDataSource指定数据源，不加注解或者不写value默认为primary指定的数据源(未指定取第一个配置的数据源)

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

## 多数据源事务
通过@WithDataSource(tx = true)来打开本地事务
```java
/**
 * 模拟嵌套事务
 * <p>
 * </p>ds组件上下文切换是用aop实现的，因为aop的特性原因，不支持同类中方法嵌套调用。直接嵌套调用会单纯认为是方法调用，aop辐射不到，
 * 所以可以用AopContext、自己注入自己、新建另一个类调用等方式来解决。
 * <p>例子这里使用AopContext来调用自己来保证都被aop识别（使用aopContext来调用本类其他方法需要设置aop的exposeProxy为true，不然会抛错）
 *
 * @author wyh
 * @date 2022/8/24 14:37
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@Service
public class LocalTxCarService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private final CarMapper carMapper;

    public LocalTxCarService(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "second", tx = true)
    public void ds2Throw() {
        String format = sdf.format(new Date());
        carMapper.update(3L, "ds2_" + format);
        int i = 1 / 0;
        carMapper.update(4L, "ds2_" + format);
    }

    @WithDataSource(value = "first", tx = true)
    public void nested() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2();
    }

    @WithDataSource(value = "first", tx = true)
    public void nestedThrow() {
        String format = sdf.format(new Date());
        carMapper.update(1L, "ds1_" + format);
        carMapper.update(2L, "ds1_" + format);
        ((LocalTxCarService) AopContext.currentProxy()).ds2Throw();
    }
}

```

### 支持日志打印事务ID
开启本地事务后会，会设置事务id到slf4j的MDC中，使用时直接添加`[%X{localTxId}]`即可
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration>
<configuration>
    <!-- 控制台 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                [%X{localTxId}][ %-5level] [%date{yyyy-MM-dd HH:mm:ss}] [%thread] %logger{96} [%line] - %msg%n
            </pattern>
            <charset>UTF-8</charset> <!-- 此处设置字符集 -->
        </encoder>
    </appender>
    <root>
        <appender-ref ref="ALL_FILE"/>
        <appender-ref ref="ERR_FILE"/>
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

[《看透，Spring是如何支持多数据源的》]:https://mp.weixin.qq.com/s/at-QJjpFi3PK7jyk0hCwcA

[Dynamic-Datasource]:https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter