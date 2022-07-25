package com.scindapsus.lock;

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
public class LockController {

    @Autowired
    private MyLockService myLockService;

    @GetMapping
    public String test(Request request) {
        return myLockService.getName(request.getName(), request.getAge());
    }

    @ExceptionHandler(TryLockException.class)
    public String bizException(TryLockException e) {
        return String.format("获取琐失败:%s", e.getMessage());
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
