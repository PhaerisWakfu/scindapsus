# Debezium  
Debezium是一个开源项目，为捕获数据更改(change data capture,CDC)提供了一个低延迟的流式处理平台。你可以安装并且配置Debezium去监控你的数据库，然后你的应用就可以消费对数据库的每一个行级别(row-level)的更改。只有已提交的更改才是可见的，所以你的应用不用担心事务(transaction)或者更改被回滚(roll back)。Debezium为所有的数据库更改事件提供了一个统一的模型，所以你的应用不用担心每一种数据库管理系统的错综复杂性。另外，由于Debezium用持久化的、有副本备份的日志来记录数据库数据变化的历史，因此，你的应用可以随时停止再重启，而不会错过它停止运行时发生的事件，保证了所有的事件都能被正确地、完全地处理掉。

监控数据库，并且在数据变动的时候获得通知一直是很复杂的事情。关系型数据库的触发器可以做到，但是只对特定的数据库有效，而且通常只能更新数据库内的状态(无法和外部的进程通信)。一些数据库提供了监控数据变动的API或者框架，但是没有一个标准，每种数据库的实现方式都是不同的，并且需要大量特定的知识和理解特定的代码才能运用。确保以相同的顺序查看和处理所有更改，同时最小化影响数据库仍然非常具有挑战性。

Debezium提供了模块为你做这些复杂的工作。一些模块是通用的，并且能够适用多种数据库管理系统，但在功能和性能方面仍有一些限制。另一些模块是为特定的数据库管理系统定制的，所以他们通常可以更多地利用数据库系统本身的特性来提供更多功能。

### 引入依赖  
```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-dbzm-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>io.debezium</groupId>
    <artifactId>debezium-connector-mysql</artifactId>
</dependency>
```

### 直接使用可添加yml配置  
```yaml
scindapsus:
  dbzm:
    datasource:
      my-connector:
        snapshot-mode: schema_only
        connector-type: mysql
        storage-file: F:/debezium/storage_my.dat
        history-file: F:/debezium/history_my.dat
        flush-interval: 10000
        server-id: 1
        server-name: mysql-1
        hostname: 127.0.0.1
        port: 3306
        user: root
        password: root
        database-whitelist: show
```

### 注册自己的CDCEvent  
```java
package com.scindapsus.dbzm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wyh
 * @since 2023/8/21
 */
@Slf4j
@Component
public class MyCDCEvent implements CDCEvent {
    @Override
    public void listen(ChangeData data) {
        log.info("table ==>{}", data.getSource().getDb() + "." + data.getSource().getTable());
        log.info("operation type ==>{}", data.getOp());
        log.info("plan name before ==>{}", data.getBefore().get("name"));
        log.info("plan name after ==>{}", data.getAfter().get("name"));
    }
}
```