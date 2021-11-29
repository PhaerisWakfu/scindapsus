package com.scindapsus.apt;

import com.scindapsus.apt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author wyh
 * @date 2021/11/29 11:16
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = APTApplication.class)
@AutoConfigureMockMvc
class APTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Test
    void test(){
        userRepository.findAll();
    }
}
