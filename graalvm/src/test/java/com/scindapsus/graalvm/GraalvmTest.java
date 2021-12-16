package com.scindapsus.graalvm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * @author wyh
 * @since 1.0
 */
class GraalvmTest {

    @Test
    void test() throws FileNotFoundException {
        String value = GraalVMUtil.execute("js", ResourceUtils.getFile("classpath:scripts/my.js"),
                "hello", String.class, "world");
        Assertions.assertEquals("hello world", value);

        Double random = GraalVMUtil.execute("js", "Math.random", Double.class);
        Assertions.assertNotNull(random);
    }
}
