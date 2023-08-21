package com.scindapsus.dbzm;

import com.scindapsus.DebeziumApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author wyh
 * @since 2022/10/14
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DebeziumApplication.class)
@AutoConfigureMockMvc
public abstract class BaseTest {

    @Autowired
    private MockMvc mockMvc;
}
