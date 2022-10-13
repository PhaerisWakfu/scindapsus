# Scindapsus

[![OSCS Status](https://www.oscs1024.com/platform/badge/phaeris/scindapsus.git.svg?size=small)](https://www.murphysec.com/dr/AiSOYD4DrZnUzl4OvV)  

------

- [lock]：封装spring官方实现的分布式锁，支持注解及SPEL

- [log]：一行配置引入日志脱敏

- [surl]：一键配置短网址服务

- [drools]：四个类封装Drools规则引擎,简化为工具类调用

- [graalvm]：使用GraalVM调用其他语言方法

- [multi-ds]：一个启动注解+数据源配置搞定多数据源

- [tenant]：调用链中自动传播租户与日志打印租户

- [calcite]： Any data, Anywhere to SQL

[lock]:/lock/README.md

[log]:/log/README.md

[surl]:/surl/README.md

[drools]:/drools/README.md

[graalvm]:/graalvm/README.md

[multi-ds]:/multi-ds/README.md

[robot]:/robot/README.md

[tenant]:/tenant/README.md

[calcite]:/calcite/README.md


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