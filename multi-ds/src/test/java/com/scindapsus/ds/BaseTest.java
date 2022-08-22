package com.scindapsus.ds;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author wyh
 * @date 2021/4/9 11:37
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DSApplication.class)
@AutoConfigureMockMvc
public abstract class BaseTest {

    @Autowired
    private MockMvc mockMvc;
}
