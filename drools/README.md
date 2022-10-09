# Scindapsus Drools  
封装规则引擎drools。 三步完成规则引擎使用：
1. 引入依赖  
```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-drools-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. 自行编写drools规则文件 & 配置规则文件路径  
```yaml
scindapsus:
  drools:
    rules-path: ${yourPath}/
```

3. 使用
```java
@RequestMapping("/drools")
@RestController
public class DroolsController {

    @GetMapping
    public Order test(Order order) {
        return DroolsHolder.fire(order);
    }
}
```