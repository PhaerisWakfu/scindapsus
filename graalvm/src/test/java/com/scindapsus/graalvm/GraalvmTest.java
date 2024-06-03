package com.scindapsus.graalvm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wyh
 * @since 2022/7/25
 */
class GraalvmTest {

    @Test
    void testLocal() throws FileNotFoundException {
        //本地
        String value = GraalVMUtil.execute("js", ResourceUtils.getFile("classpath:scripts/my.js"),
                "hello", String.class, "world");
        Assertions.assertEquals("hello world", value);
    }

    @Test
    void testInherit() {
        //内置
        Double random = GraalVMUtil.execute("js", "Math.random", Double.class);
        Assertions.assertNotNull(random);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void testRemote() throws IOException {
        //远程
        URL url = new URL("https://xxx/test1.js");
        File file = null;
        Integer remoteVal;
        try {
            file = downloadRemoteFile(url);
            remoteVal = GraalVMUtil.execute("js", file,
                    "layout", Integer.class);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
        Assertions.assertEquals(100, remoteVal);
    }

    private File downloadRemoteFile(URL remoteUrl) throws IOException {
        // 创建一个临时文件
        File tempFile = File.createTempFile("remote-js-", ".js");

        // 获取远程文件的连接
        URLConnection connection = remoteUrl.openConnection();

        // 从远程文件读取数据并写入临时文件
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }
}
