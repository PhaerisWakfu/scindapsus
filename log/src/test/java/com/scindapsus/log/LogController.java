package com.scindapsus.log;

import com.alibaba.fastjson.JSON;
import com.scindapsus.log.annotation.OPLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wyh
 * @since 1.0
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    @GetMapping
    @OPLog
    public String test(Request request) {
        log.info("request is {}", JSON.toJSONString(request));
        log.info(JSON.toJSONString(request));
        return "ok";
    }

    public static class Request {

        private String name;

        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
