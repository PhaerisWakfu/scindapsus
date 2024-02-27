package com.scindapsus.screenshot;

import java.io.IOException;

/**
 * @author wyh
 * @since 2023/12/27
 */
public class ScreenshotTest {

    public static void main(String[] args) throws IOException {
        String driverPath = "D:\\soft\\chrome driver\\chromedriver.exe";
        //windows本地环境如果装有chrome可以不填binaryPath
        Screenshot.takeScreenshot(driverPath, null, "https://gitee.com/phaeris/scindapsus", "C:\\Users\\A11-9\\Desktop\\send\\scindapsus.png");
    }
}
