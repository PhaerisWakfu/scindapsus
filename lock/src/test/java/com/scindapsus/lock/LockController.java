package com.scindapsus.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wyh
 * @since 1.0
 */
@RestController
@RequestMapping("/lock")
@Slf4j
public class LockController {

    @Autowired
    private MyLockService myLockService;

    @GetMapping
    public String test(Request request) {
        return myLockService.getName(request.getName(), request.getAge());
    }

    @ExceptionHandler(TryLockException.class)
    public String bizException(TryLockException e) {
        log.error("获取琐失败", e);
        return String.format("获取琐失败:%s", e.getMessage());
    }

    @Data
    public static class Request {
        private String name;
        private String age;
    }
}
