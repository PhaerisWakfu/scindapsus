# Scindapsus Pulsar

## 说明

简单封装Apache Pulsar为starter


## 开始使用

### 引入依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-pulsar-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 添加配置

```yaml
scindapsus:
  pulsar:
    #客户端
    client:
      #名称（必填）
      name: test
      #认证className
      auth-class-name: com.scindapsus.pulsar.MyAuthenticationProvider
      #client端相关配置
      property:
        #连接地址
        service-url: pulsar://localhost:6650
    #消费者配置支持多个（名称:配置）
    consumer:
      consumer1:
        #message schema类名(实现{@link org.apache.pulsar.client.api.Schema}或{@link com.scindapsus.pulsar.PulsarSchemaProvider})
        schema-class-name: com.scindapsus.pulsar.MyPulsarSchemaProvider
        #listener类名
        listener-class-name: com.scindapsus.pulsar.ProducerListener
        subscription-type: Shared
        subscription-initial-position: Latest
        property:
          topic-names: test-str
          subscription-name: MyConsumer
      consumer2:
        schema-class-name: com.scindapsus.pulsar.MyPulsarSchemaProvider
        listener-class-name: com.scindapsus.pulsar.ProducerListener
        subscription-type: Shared
        subscription-initial-position: Latest
        property:
          topic-names: test-str
          subscription-name: MyConsumer
    #生产者配置支持多个（名称:配置）
    producer:
      producer1:
        #message schema类名(实现{@link org.apache.pulsar.client.api.Schema}或{@link com.scindapsus.pulsar.PulsarSchemaProvider})
        schema-class-name: com.scindapsus.pulsar.MyPulsarSchemaProvider
        access-mode: Shared
        compression-type: LZ4
        property:
          topic-name: test-str
          producer-name: MyProducer
```

### 开启自动装配
```java
@EnablePulsar
@SpringBootApplication
public class PulsarTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PulsarTestApplication.class, args);
    }
}

```

### 创建自定义schemaProvider
```java
public class MyPulsarSchemaProvider implements PulsarSchemaProvider<String> {
    
    @Override
    public Schema<String> get() {
        return Schema.STRING;
    }
}
```

### 创建listener

```java
public class ProducerListener implements MessageListener<String> {

    @Override
    public void received(Consumer<String> consumer, Message<String> msg) {
        LOGGER.info("sub[{}] name[{}] listener ->{}", consumer.getSubscription(), consumer.getConsumerName(), msg.getValue());
        try {
            consumer.acknowledge(msg);
        } catch (PulsarClientException e) {
            consumer.negativeAcknowledge(msg);
        }
    }
}
```

### 创建发送者
```java
@Component
public class PulsarSender {

    private final Producer<String> producer1;

    public PulsarSender(Producer<String> producer1) {
        this.producer1 = producer1;
    }

    public CompletableFuture<MessageId> send(String message) {
        return producer1.sendAsync(message);
    }
}
```

### 请求

```java
@RestController
@RequestMapping("/pulsar")
public class PulsarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarController.class);

    @Autowired
    private PulsarSender pulsarSender;

    @GetMapping
    public String send() {
        LOGGER.info("send");
        pulsarSender.send("hello");
        return "success";
    }
}
```

```http
###
GET http://localhost:7070/pulsar
```