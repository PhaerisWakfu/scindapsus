

# Scindapsus Screenshot

[toc]


## 说明

- 实现输入网址实时截图



## 开始使用

### 依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-screenshot</artifactId>
</dependency>
```

#### 使用

```java
    public static void main(String[] args) throws IOException {
        String driverPath = "D:\\soft\\chrome driver\\chromedriver.exe";
        //windows本地环境如果装有chrome可以不填
        Screenshot.takeScreenshot(driverPath, null, "https://gitee.com/phaeris/scindapsus", "C:\\Users\\A11-9\\Desktop\\send\\scindapsus.png");
        }
```