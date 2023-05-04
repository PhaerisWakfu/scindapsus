package com.scindapsus.lock;

import com.scindapsus.lock.annotation.DistributedLock;
import com.scindapsus.lock.exception.DistributedLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wyh
 * @since 2021/10/9
 */
@Service
public class MyLockService {

    @DistributedLock(name = "#p0", key = "#a1", fallback = MyFallbackFactory.class, retryDuration = 1000)
    public String getName(String name, String age) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new DistributedLockException("sleep interrupted exception");
        }
        return "ok";
    }

    @Component
    public static class MyFallbackFactory implements LockFallback<MyLockService> {

        private static final Logger log = LoggerFactory.getLogger(MyFallbackFactory.class);

        @Override
        public MyLockService create(Throwable cause) {
            return new MyLockService() {
                @Override
                public String getName(String name, String age) {
                    log.error("加锁失败回调cause {}", cause.getMessage());
                    throw new TryLockException("模拟加锁失败处理", cause);
                }
            };
        }
    }
}
