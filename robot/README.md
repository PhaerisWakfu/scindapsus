# Scindapsus Robot

## 依赖

```
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
public class MyRobotServiceImpl implements RobotService {

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
/**
 * @author wyh
 * @date 2022/8/29 15:08
 */
public class RobotTest extends BaseTest {

    @Autowired
    private MyRobotServiceImpl myRobotServiceImpl;

    @Test
    public void send() {
        System.out.println(myRobotServiceImpl.sendMsg("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=60e707a9-609d-4e24-9a95-de39660023e5",
                "hello $brand$", "select brand from car where id=1"));
    }
}
```