package com.scindapsus.drools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author wyh
 * @since 2022/7/25
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroolsTestApplication.class)
@AutoConfigureMockMvc
class DroolsTests {

    @Test
    void test() {
        Assertions.assertEquals(80d, DroolsHolder.fire(new Order(100d, null)).getRealPrice());
    }
}
