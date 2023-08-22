# Scindapsus Robot

> - 主要用作群聊推送，目前支持企业微信的群聊机器人与钉钉的自定义机器人。  
> - 消息内容支持stringTemplate模板语法，并且支持通过sql来设置模板中的变量，可以达到灵活动态推送消息的目的。  
> - 可以搭配quartz等定时框架，定时推消息提醒等。

## 依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.phaeris.scindapsus</groupId>
        <artifactId>scindapsus-robot-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

    <!--test-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 使用

### 配置所需的RestTemplate和JdbcTemplate
如果自己应用中已经有所需要的bean可忽略这步

```java
@Configuration
public class MyConfig {

    /**
     * 必须注册，需要向机器人hook地址发送http请求
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 如果只需要发送固定简单消息（不带SQL）的可以不注册
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        String url = "jdbc:mysql://localhost:3306/ds1?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "root";
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        return JdbcUtil.jdbcTemplate(url, driverClassName, username, password);
    }
}
```

### 发送  
  - template中获取参数使用分隔符$，例如`$name$`
  - 企微的markdown消息与钉钉消息支持markdown语法
  - sql语句查出的参数名称以sql字段名为准，注意别名，编写变量时要注意
  - sql结果可以是list，但是list的变量名固定为`params`，使用例子详见下面的multiSQLResult方法

```java
public class RobotTest extends BaseTest {

    @Autowired
    private RobotMsgSender robotMsgSender;

    /**
     * 固定简单消息
     */
    @Test
    public void wechatNormalMsg() {
        SendResultDTO result = robotMsgSender.sendWxTxtMsg(
                "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=60e707a9-609d-4e24-9a95-de39660023e5",
                "hello world",
                null);
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 带SQL变量的复杂消息,需要有jdbcTemplate
     */
    @Test
    public void wechat() {
        SendResultDTO result = robotMsgSender.sendWxTxtMsg(
                "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx",
                "hello $brand$",
                "select brand from car where id=1",
                "@all");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 单行SQL变量的消息
     */
    @Test
    public void dingTalk() {
        SendResultDTO result = robotMsgSender.sendDingMsg(
                "https://oapi.dingtalk.com/robot/send?access_token=xxx",
                "scindapsus",
                "hello $brand$",
                "xxx",
                "select brand from car where id=1",
                "@all");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 多行SQL变量的消息
     */
    @Test
    public void multiSQLResult() {
        SendResultDTO result = robotMsgSender.sendDingMultiRstMsg(
                "https://oapi.dingtalk.com/robot/send?access_token=xxx",
                "scindapsus",
                "#### 总数==>$first(params).ct$\n" +
                        "$params:{p|\n" +
                        "> 名称：$p.name$，品牌:$p.brand$\n" +
                        "};separator=\"\\n\"$",
                "xxx",
                "SELECT t1.*, t2.* FROM( SELECT `name`, `brand` FROM `car` u) t1, (SELECT count(1) ct FROM `car`) t2",
                "18321809917");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }
}
```