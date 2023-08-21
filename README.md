# Scindapsus

[![OSCS Status](https://www.oscs1024.com/platform/badge/phaeris/scindapsus.git.svg?size=small)](https://www.murphysec.com/dr/AiSOYD4DrZnUzl4OvV)  

------

- [lock]：封装spring官方实现的分布式锁，支持静态获取琐、注解式加琐与动态代理加锁

- [log]：一行配置引入日志脱敏

- [surl]：一键配置短网址服务

- [drools]：四个类封装Drools规则引擎,简化为工具类调用

- [graalvm]：使用GraalVM调用其他语言方法

- [multi-ds]：一个启动注解+数据源配置搞定多数据源

- [robot]：企业微信群聊机器人/钉钉自定义机器人简单封装，支持antlr的stringTemplate模板语法与从SQL中获取变量参数

- [tenant]：调用链中自动传播租户与日志打印租户

- [calcite]：无需懂calcite语法，简单配置通过SQL读取异构数据

- [pulsar]： apache pulsar spring boot starter

- [dbzm]： debezium, 比canal更强大易用，支持数据库类型更多的的cdc框架

[lock]:/lock/README.md

[log]:/log/README.md

[surl]:/surl/README.md

[drools]:/drools/README.md

[graalvm]:/graalvm/README.md

[multi-ds]:/multi-ds/README.md

[robot]:/robot/README.md

[tenant]:/tenant/README.md

[calcite]:/calcite/README.md

[pulsar]:/pulsar/README.md

[dbzm]:/dbzm/README.md


### 安装
`mvn clean install -DskipTests -T 1C`

### 引入pom依赖管理
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.phaeris.scindapsus</groupId>
            <artifactId>scindapsus-dependencies</artifactId>
            <version>${scindapsus.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Star History
[![Star History Chart](https://api.star-history.com/svg?repos=PhaerisWakfu/scindapsus&type=Date)](https://api.star-history.com/svg?repos=PhaerisWakfu/scindapsus&type=Date)