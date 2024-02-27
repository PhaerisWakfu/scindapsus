

# Scindapsus Screenshot

[toc]


## 说明

- 使用无头浏览器实现输入网址获取实时截图


## 开始使用

### 下载chrome浏览器及其相对应的chromedriver
一定要版本对应，不然启动时会提示你版本不符的
```
https://googlechromelabs.github.io/chrome-for-testing/#stable
```

### 依赖

```xml
<dependency>
    <groupId>com.phaeris.scindapsus</groupId>
    <artifactId>scindapsus-screenshot</artifactId>
</dependency>
```

#### 使用

```java
/**
 * @author wyh
 * @since 2023/12/27
 */
public class ScreenshotTest {

    public static void main(String[] args) throws IOException {
        String driverPath = "D:\\soft\\chrome driver\\chromedriver.exe";
        //windows本地环境如果装有chrome可以不填binaryPath(也就是chrome的二进制文件)
        Screenshot.takeScreenshot(driverPath, null, "https://gitee.com/phaeris/scindapsus", "C:\\Users\\A11-9\\Desktop\\send\\scindapsus.png");
    }
}
```