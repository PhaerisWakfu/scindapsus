# Scindapsus Short Url



[toc]

## 说明

简单配置快速生成短链生成服务



## 开始使用

### 引入依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-surl-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 添加配置

```yaml
scindapsus:
  surl:
    #是否打开默认提供的API(默认false)
    enabled: true
    #默认提供的API路径,enabled为true时必填
    path: /surl
```

### 请求

```http
### 长转短
POST http://localhost:8080/surl
Accept: application/json
Content-Type: application/json

{
  "originalUrl": "www.baidu.com"
}
```

### 工具类

不打开默认提供的API时，可以自行使用工具类UrlMappingHolder来实现

```java
@Controller
public class TestController {


    @PostMapping("/dwz")
    @ResponseBody
    public String convert(@RequestBody String url) {
        return UrlMappingHolder.mapping(url, "http://localhost:8080");
    }

    @GetMapping("/dwz/{key}")
    public void routing(@PathVariable String key) throws IOException {
        response.sendRedirect(UrlMappingHolder.find(key));
    }
}
```



## 短网址存放SPI

可以自行实现UrlMappingService注册为bean来设置短网址存取，例如mysql、redis等，默认使用Caffeine存在内存中(详见源码com.scindapsus.surl.config.ShortUrlConfiguration.DefaultShortUrlServiceImpl)

```java
@Component
public class MyUrlMappingServiceImpl implements UrlMappingService {

    private static final Map<String, String> myMapping = new ConcurrentHashMap<>();

    @Override
    public void save(ShortUrl shortUrl) {
        myMapping.put(shortUrl.getKey(), shortUrl.getOriginalUrl());
    }

    @Override
    public String get(String key) {
        return myMapping.get(key);
    }
}
```