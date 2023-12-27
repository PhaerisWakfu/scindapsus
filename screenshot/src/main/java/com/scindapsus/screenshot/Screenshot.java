package com.scindapsus.screenshot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wyh
 * @since 2023/10/26
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Screenshot {

    private static final Integer DEFAULT_WAIT_MILLS = 1000;

    private static final Integer DEFAULT_HEIGHT = 1080;

    private static final Integer DEFAULT_WIDTH = 1920;

    private static final String DEFAULT_FORMAT = "PNG";

    private static final String SCRIPT_GET_BODY_HEIGHT = "return document.body.offsetHeight";

    private static final String SCRIPT_GET_ELE_HEIGHT = "return document.querySelectorAll('%s')[0].offsetHeight";


    /**
     * 截图并输出
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     * @param output     输出
     */
    public static void takeScreenshot(String driverPath, String binaryPath, String pageUrl, String output) throws IOException {
        takeScreenshot(driverPath, binaryPath, pageUrl, null, null, null, DEFAULT_FORMAT, output);
    }

    /**
     * 截图并输出
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     * @param output     输出
     */
    public static void takeScreenshot(String driverPath, String binaryPath, String pageUrl, OutputStream output) throws IOException {
        takeScreenshot(driverPath, binaryPath, pageUrl, null, null, null, DEFAULT_FORMAT, output);
    }

    /**
     * 截图并输出
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     * @param ele        滚动元素
     * @param width      宽(默认1920)
     * @param waitMills  页面等待加载时间（ms）
     * @param formatName 文件后缀名
     * @param output     输出
     */
    public static void takeScreenshot(String driverPath, String binaryPath, String pageUrl, String ele,
                                      Integer width, Integer waitMills, String formatName, String output) throws IOException {
        //截图
        BufferedImage screenshot = takeScreenshot(driverPath, binaryPath, pageUrl, ele, width, waitMills);
        //输出
        ImageIO.write(screenshot, formatName, new File(output));
    }

    /**
     * 截图并输出
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     * @param ele        滚动元素
     * @param width      宽(默认1920)
     * @param waitMills  页面等待加载时间（ms）
     * @param formatName 文件后缀名
     * @param output     输出
     */
    public static void takeScreenshot(String driverPath, String binaryPath, String pageUrl, String ele,
                                      Integer width, Integer waitMills, String formatName, OutputStream output) throws IOException {
        //截图
        BufferedImage screenshot = takeScreenshot(driverPath, binaryPath, pageUrl, ele, width, waitMills);
        //输出
        ImageIO.write(screenshot, formatName, output);
    }

    /**
     * 截长图
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     */
    public static BufferedImage takeScreenshot(String driverPath, String binaryPath, String pageUrl) {
        return takeScreenshot(driverPath, binaryPath, pageUrl, null, null, null);
    }

    /**
     * 截长图
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @param pageUrl    页面url
     * @param ele        滚动元素
     * @param width      宽(默认1920)
     * @param waitMills  页面等待加载时间（ms）
     */
    public static BufferedImage takeScreenshot(String driverPath, String binaryPath, String pageUrl,
                                               String ele, Integer width, Integer waitMills) {

        //这里每次新建driver(处理并发调用下使用同一个实例会互相影响宽高的问题)
        WebDriver driver = getDriver(driverPath, binaryPath);

        try {

            //初始高度
            int w = width != null && width > 0 ? width : DEFAULT_WIDTH;
            driver.manage().window().setSize(new Dimension(w, DEFAULT_HEIGHT));

            //请求页面
            driver.get(pageUrl);

            //等待页面加载
            TimeUnit.MILLISECONDS.sleep(Optional.ofNullable(waitMills).orElse(DEFAULT_WAIT_MILLS));

            //计算设置真实高度
            String script = Optional.ofNullable(ele)
                    .map(e -> String.format(SCRIPT_GET_ELE_HEIGHT, e))
                    .orElse(SCRIPT_GET_BODY_HEIGHT);
            Long offsetHeight = (Long) ((JavascriptExecutor) driver).executeScript(script);
            int h = offsetHeight != null && offsetHeight > 0 ? offsetHeight.intValue() + 100 : DEFAULT_HEIGHT;
            driver.manage().window().setSize(new Dimension(w, h));

            //滚动截图
            AShot aShot = new AShot()
                    .coordsProvider(new WebDriverCoordsProvider())
                    .shootingStrategy(ShootingStrategies.viewportPasting(1000));
            return Optional.ofNullable(ele)
                    .map(e -> aShot.takeScreenshot(driver, driver.findElement(By.cssSelector(e))))
                    .orElse(aShot.takeScreenshot(driver))
                    .getImage();
        } catch (Exception e) {
            throw new ScreenshotException("write failed", e);
        } finally {
            //因为是每次新建, 所以这里每次都关闭所有窗口关闭会话
            driver.close();
            driver.quit();
        }
    }

    /**
     * 获取driver
     *
     * @param driverPath 驱动路径
     * @param binaryPath 浏览器二进制文件路径
     * @return driver
     */
    public static WebDriver getDriver(String driverPath, String binaryPath) {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
        Optional.ofNullable(binaryPath).ifPresent(chromeOptions::setBinary);
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--headless");//设置为 headless 模式 （必须）
        chromeOptions.addArguments("--disable-gpu");//谷歌文档提到需要加上这个属性来规避bug
        chromeOptions.addArguments("--no-sandbox");//参数是让Chrome在root权限下跑
        chromeOptions.addArguments("--disable-dev-shm-usage");//禁用 Chrome 浏览器在共享内存文件系统（dev/shm）上使用临时文件
        chromeOptions.addArguments("lang=zh_CN.UTF-8");//中文
        chromeOptions.addArguments("--no-zygote");//处理僵尸进程
        return new ChromeDriver(chromeOptions);
    }

    /**
     * 关闭窗口并保持会话
     *
     * @param driver 浏览器驱动
     */
    public static void close(WebDriver driver) {
        Set<String> windowHandles = driver.getWindowHandles();
        //close关闭时如果是最后一个窗口或标签页会关闭浏览器会话
        //大于1个才关闭, 避免全局实例会话关闭
        if (windowHandles != null && !windowHandles.isEmpty() && windowHandles.size() > 1) {
            driver.close();
        }
    }
}