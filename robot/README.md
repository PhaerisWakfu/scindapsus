# Scindapsus Robot

## 依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.phaeris.scindapsus</groupId>
        <artifactId>scindapsus-robot</artifactId>
        <version>1.0-SNAPSHOT</version>
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

### 配置自己的service
实现RobotService,如果自己应用中有restTemplate直接注入,如果获取数据与项目自身数据源一致也直接注入

```java
/**
 * @author wyh
 * @date 2022/8/29 15:04
 */
@Service
public class MyRobotServiceImpl extends RobotService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JdbcTemplate setJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public RestTemplate setRestTemplate() {
        return restTemplate;
    }
}
```

###发送  
  - template中获取参数使用分隔符$，例如$name$ 
  - template支持markdown语法
  - sql语句查出的参数名称以sql字段名为准，注意别名，编写变量时要注意
  - sql结果不可以是list，只支持单行结果

```java
public class RobotTest extends BaseTest {

    @Autowired
    private MyRobotServiceImpl myRobotServiceImpl;

    @Test
    public void wechat() {
        SendResultDTO result = myRobotServiceImpl.wechatTxtMsg(
                "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx",
                "hello $brand$",
                "select brand from car where id=1",
                "@all");
    }

    @Test
    public void dingTalk() {
        SendResultDTO result = myRobotServiceImpl.dingTalkMsg(
                "https://oapi.dingtalk.com/robot/send?access_token=xxx",
                "scindapsus",
                "hello $brand$",
                "xxx",
                "select brand from car where id=1",
                "@all");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    @Test
    public void multiSQLResult() {
        SendResultDTO result = myRobotServiceImpl.dingTalkMultiRetMsg(
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