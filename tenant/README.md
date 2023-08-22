# Scindapsus Tenant



[toc]

## 说明

使用spring cloud sleuth在调用链上传递租户并支持日志中打印租户



## 开始使用

### 引入依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-tenant-spring-cloud-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 添加配置

```yaml
scindapsus:
  tenant:
    #租户key
    name: x-wyh-tenant
    #租户设置与传播
    propagation:
      #开关
      enabled: true
      #设置与传播的路径
      patterns: /tenant/**,/company/**
```

### 自定义tenantProvider

默认tenantProvider直接取scindapsus.tenant.name，如需自定义可实现TenantProvider

```java
@Configuration
public class MyConfig {

    @Bean
    public TenantProvider tenantProvider() {
        return request -> request.getHeader("company-id");
    }
}
```

### 模拟服务间调用
```java
@RestController
@RequestMapping({"/tenant", "/company"})
public class MyTenantController {

    private final Logger log = LoggerFactory.getLogger(MyTenantController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test1")
    public String test1() {
        log.info("服务8080开始调用服务8081");
        return restTemplate.getForObject("http://localhost:8081/company/test2", String.class);
    }

    @GetMapping("/test2")
    public String test2() {
        log.info("服务8081被调用了");
        return Optional.ofNullable(TenantHolder.getTenant()).orElse("未获取到租户");
    }
}
```

### 请求

```http
###
GET http://localhost:8080/tenant/test1
company-id: hello
```

## 支持日志中打印租户ID

### 添加配置

```yaml
scindapsus:
  tenant:
    #租户key
    name: x-wyh-tenant
    #租户设置与传播
    propagation:
      #开关
      enabled: true
      #设置与传播的路径
      patterns: /tenant/**,/company/**
    #是否将租户放入slf4j的MDC中（默认true）
    mdc: true
```
### 编写日志配置文件
日志打印租户只需添加%X{你设置的租户key}
```xml
<appender name="ALL_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <encoder>
        <pattern>
            [%X{traceId}][%X{spanId}][%X{x-wyh-tenant}][ %-5level] [%date{yyyy-MM-dd HH:mm:ss}] [%thread] %logger{96} [%line] - %msg%n
        </pattern>
        <charset>UTF-8</charset> <!-- 此处设置字符集 -->
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <!-- rollover daily 配置日志所生成的目录以及生成文件名的规则 -->
        <fileNamePattern>${LOG_PATH}/log-total-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <!-- 除按日志记录之外，还配置了日志文件不能超过100M，若超过100M，日志文件会以索引0开始，
        命名日志文件，例如log-error-2013-12-21.0.log -->
        <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
            <maxFileSize>100MB</maxFileSize>
        </timeBasedFileNamingAndTriggeringPolicy>
    </rollingPolicy>
    <!-- Safely log to the same file from multiple JVMs. Degrades performance! -->
    <prudent>true</prudent>
</appender>
```

## 自定义设置tenant
```java
TenantHolder.setTenant(myTenant);
```