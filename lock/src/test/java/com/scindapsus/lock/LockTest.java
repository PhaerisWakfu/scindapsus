package com.scindapsus.lock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author wyh
 * @since 2021/10/9
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LockTestApplication.class)
class LockTest {

    @Autowired
    private MyLockService myLockService;

    @Test
    void tryLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<String> future1 = executorService.submit(() -> myLockService.getName("wyh", "18"));
        Future<String> future2 = executorService.submit(() -> myLockService.getName("wyh", "18"));
        try {
            future1.get();
            future2.get();
        } catch (Exception e) {
            e.printStackTrace();
            //期望调用回调方法时抛错throw new TryLockException
            Assertions.assertEquals("com.scindapsus.lock.exception.DistributedLockException: Invoke fallback exception.", e.getMessage());
        }
    }
}
