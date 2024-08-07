# Scindapsus

[![OSCS Status](https://www.oscs1024.com/platform/badge/phaeris/scindapsus.git.svg?size=small)](https://www.murphysec.com/dr/AiSOYD4DrZnUzl4OvV)

------

- [calcite]：无需懂calcite语法，简单配置通过SQL读取异构数据
- [dbzm]： debezium, 比canal更强大易用，支持数据库类型更多的的cdc框架
- [drools]：drools spring boot starter
- [graalvm]：使用GraalVM调用其他语言方法
- [lock]：封装spring官方实现的分布式锁，支持静态获取锁、注解式加锁与动态代理加锁
- [log]：一行配置引入日志脱敏
- [multi-ds]：一个启动注解+数据源配置搞定多数据源
- [pulsar]： apache pulsar spring boot starter
- [robot]：企业微信钉钉机器人简单封装，支持模板语法+SQL动态填充
- [screenshot]：使用无头浏览器实现输入网址获取实时截图
- [surl]：一键配置短网址服务
- [tenant]：调用链中自动传播租户与日志打印租户

[calcite]:/calcite/README.md

[dbzm]:/dbzm/README.md

[drools]:/drools/README.md

[graalvm]:/graalvm/README.md

[lock]:/lock/README.md

[log]:/log/README.md

[multi-ds]:/multi-ds/README.md

[pulsar]:/pulsar/README.md

[robot]:/robot/README.md

[screenshot]:https://github.com/PhaerisWakfu/leica

[surl]:/surl/README.md

[tenant]:/tenant/README.md

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