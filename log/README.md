

# Scindapsus Log

## 说明

- 支持添加注解后统一格式打印请求日志功能(请求路径、出参、入参、响应时间、traceId、userId等)
- 支持日志脱敏

## 开始使用

### 依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-log-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>


<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-json</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

<!--如需记录traceId可选-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```


### 打印请求日志

添加注解OPLog

```java
/**
 * @author wyh
 * @date 2021/9/18 17:15
 */
@RestController
@RequestMapping("/scindapsus")
@Slf4j
public class ScindapsusController {

    @PostMapping
    @OPLog
    public String test(@RequestBody Request request) {
        return "hello";
    }

    @Data
    public static class Request {
        private String name;
        private String age;
    }
}
```



#### 支持SPI获取userId与traceId

记录userId

```java
import com.scindapsus.log.user.UserProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author wyh
 * @date 2021/10/9 15:17
 */
@Component
public class MyUserProvider implements UserProvider<String> {

    @Override
    public String getUserId() {
        return UUID.randomUUID().toString();
    }
}
```

记录traceId只需引入sleuth包

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```



#### 测试

```http
POST http://localhost:8080/scindapsus
Accept: application/json
Content-Type: application/json

{
  "name": "wyh",
  "age": "18"
}
```



### 日志脱敏

logback配置文件中添加配置

```xml
    <!--scindapsus日志脱敏-->
    <include resource="logback-desensitize.xml"/>
    <!--开关,默认false-->
    <property scope="context" name="DesensitizeEnabled" value="true"/>
    <!--format支持:
        //用户id
        USER_ID,
        //中文名
        CHINESE_NAME,
        //身份证号
        ID_CARD,
        //座机号
        FIXED_PHONE,
        //手机号
        MOBILE_PHONE,
        //地址
        ADDRESS,
        //电子邮件
        EMAIL,
        //密码
        PASSWORD,
        //中国大陆车牌，包含普通车辆、新能源车辆
        CAR_LICENSE,
        //银行卡
        BANK_CARD
    -->
    <property scope="context" name="SensitiveDataKeys" value='[{"fieldName":"name","format":"CHINESE_NAME"}]'/>
```



#### 使用

```java
/**
 * @author wyh
 * @date 2021/9/18 17:15
 */
@RestController
@RequestMapping("/scindapsus")
@Slf4j
public class ScindapsusController {

    @PostMapping
    public String test(@RequestBody Request request) {
        log.info("request is {}", JSON.toJSONString(request));
        log.info(JSON.toJSONString(request));
        return "hello";
    }

    @Data
    public static class Request {
        private String name;
        private String age;
    }
}
```

```http
POST http://localhost:8080/scindapsus
Accept: application/json
Content-Type: application/json

{
  "name": "wyh",
  "age": "18"
}
```



