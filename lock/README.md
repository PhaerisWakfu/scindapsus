# Scindapsus Lock



[toc]



## 前言

### 常规实现的隐患

一般情况下我们自行实现的Redis分布式锁存在以下问题：

##### SETNX 和 EXPIRE 非原子性

如果 SETNX 成功，在设置锁超时时间后，服务器挂掉、重启或网络问题等，导致 EXPIRE 命令没有执行，锁没有设置超时时间变成死锁。



解决方案：

- lua脚本
- 使用2.6.12之后的set key value time NX命令来替代



##### 锁误解除

如果线程 A 成功获取到了锁，并且设置了过期时间 30 秒，但线程 A 执行时间超过了 30 秒，锁过期自动释放，此时线程 B 获取到了锁；随后 A 执行完成，线程 A 使用 DEL 命令来释放锁，但此时线程 B 加的锁还没有执行完成，线程 A 实际释放的线程 B 加的锁。



解决方案：

- 充分利用加锁时的value，生成UUID或线程号放入value，释放锁时候判断是不是自己的锁；因为判断与释放不是原子操作，需要配合lua脚本



##### 超时解锁导致并发

锁误接触时候，虽然我们避免了线程A误删掉key的情况，但是同一时间有A，B两个线程在访问代码块，仍然是不完美的。



解决方案：

- 将过期时间设置足够长，确保代码逻辑在锁释放之前能够执行完成。
- 为获取锁的线程增加守护线程，为将要过期但未释放的锁增加有效时间。



##### 不可重入

当线程在持有锁的情况下再次请求加锁，如果一个锁支持一个线程多次加锁，那么这个锁就是可重入的。如果一个不可重入锁被再次加锁，由于该锁已经被持有，再次加锁会失败。Redis 可通过对锁进行重入计数，加锁时加 1，解锁时减 1，当计数归 0 时释放锁。



解决方案：

- 使用 ThreadLocal 进行重入次数统计
- Redis Map 数据结构来实现分布式锁，既存锁的标识也对重入次数进行计数



##### 无法等待锁释放

上述命令执行都是立即返回的，如果客户端可以等待锁释放就无法使用。



解决方案：

- 通过客户端轮询的方式解决该问题，当未获取到锁时，等待一段时间重新获取锁，直到成功获取锁或等待超时。这种方式比较消耗服务器资源，当并发量比较大时，会影响服务器的效率。
- 使用 Redis 的发布订阅功能，当获取锁失败时，订阅锁释放消息，获取锁成功后释放时，发送锁释放消息



##### 集群下主备切换

为了保证 Redis 的可用性，一般采用主从方式部署。主从数据同步有异步和同步两种方式，Redis 将指令记录在本地内存 buffer 中，然后异步将 buffer 中的指令同步到从节点，从节点一边执行同步的指令流来达到和主节点一致的状态，一边向主节点反馈同步情况。

在包含主从模式的集群部署方式中，当主节点挂掉时，从节点会取而代之，但客户端无明显感知。当客户端 A 成功加锁，指令还未同步，此时主节点挂掉，从节点提升为主节点，新的主节点没有锁的数据，当客户端 B 加锁时就会成功。



##### 集群脑裂

集群脑裂指因为网络问题，导致 Redis master 节点跟 slave 节点和 sentinel 集群处于不同的网络分区，因为 sentinel 集群无法感知到 master 的存在，所以将 slave 节点提升为 master 节点，此时存在两个不同的 master 节点。Redis Cluster 集群部署方式同理。

当不同的客户端连接不同的 master 节点时，两个客户端可以同时拥有同一把锁



### 我们的实现

为了解决该问题，我们新建了统一的锁模块，使用了spring-boot-starter-integration中spring官方的分布式锁成熟实现，其支持zookeeper分布式锁、Gemfire分布式锁、JDBC分布式锁等。

我们使用注解+SPEL模式实现了spring官方的分布式锁，使客户端不再关心怎么加锁与释放，SPEL使配置更灵活，底层锁实现切换更容易。





## Redis

### 依赖

```xml
<!--快照版本-->
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-lock-spring-boot-starter</artifactId>
</dependency>
<!--redis锁实现-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-redis</artifactId>
</dependency>
```



### 配置

```yaml
scindapsus:
  lock:
    #锁类型
    type: redis
    redis:
      #锁过期时间
      expire: 60000
      #锁前缀
      registry-key: test
spring:
  redis:
    database: 0
    host: localhost
    port: 6379

logging:
  level:
    com.scindapsus: debug
```



## Zookeeper



### 依赖

```xml
<!--快照版本-->
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-lock-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--zk锁实现-->
<dependency>
    <groupId>org.springframework.integration</groupId>
    <artifactId>spring-integration-zookeeper</artifactId>
</dependency>
```



### 配置

```yaml
scindapsus:
  lock:
  #锁类型
    type: zookeeper
    zookeeper:
      #zk连接
      connection-string: 127.0.0.1:2181
      #根路径
      root: /test
      #连接重试之间等待的初始时间(milliseconds)
      base-sleep-time-ms: 1000
      #连接最大重试次数
      max-retries: 3

logging:
  level:
    com.scindapsus: debug
```



## 使用

添加注解@DistributedLock

```java
@Service
public class LockService {

    /**
     * 因为使用AOP实现,标注注解的方法需是public,并且不支持加注解的方法之间嵌套
     *
     * @name: regionName
     * @key: key，name与key拼成最终的lockName，支持spel表达式（如#key、#key.length()、#p0、#a0）
     * <p>spel更多详见
     * <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">SPEL</a>
     * </p>
     * @fallback: 失败回调类
     * @retryDuration: 获取锁失败重试时间（毫秒），默认不重试
     */
    @DistributedLock(name = "#p0.firstName", key = "#a0.lastName", fallback = Fallback.class, retryDuration = 10000)
    public void lock(Person person) {
        //do something
    }

    @Slf4j
    public class Fallback implements LockFallback<LockService>{

        @Override
        public LockService create(Throwable cause) {
            return new LockService() {
                @Override
                public void lock(Person person) {
                    log.error("reason->", cause);
                    log.info(">>>>fallback->{}", person);
                }
            };
        }
    }

    @Data
    public static class Person {

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        private String firstName;
        private String lastName;
        private String address;
        private String country;
    }
}
```