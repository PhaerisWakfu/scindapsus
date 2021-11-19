package com.scindapsus.log;

import com.alibaba.fastjson.JSON;
import com.scindapsus.log.annotation.OPLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author wyh
 * @date 2021/9/18 17:15
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @GetMapping
    @OPLog
    public String test(Request request) {
        log.info("request is {}", JSON.toJSONString(request));
        log.info(JSON.toJSONString(request));
        return "ok";
    }

    @Data
    public static class Request {
        private String name;
        private String age;
    }
}
