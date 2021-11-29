package com.scindapsus.lock;

import com.scindapsus.lock.annotation.DistributedLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wyh
 * @date 2021/9/22 13:50
 */
@Service
public class MyLockService {

    @SneakyThrows
    @DistributedLock(name = "#p0", key = "#a1", fallback = MyFallbackFactory.class, retryDuration = 1000 * 5)
    public String getName(String name, String age) {
        TimeUnit.SECONDS.sleep(10);
        return "ok";
    }

    @Slf4j
    @Component
    public static class MyFallbackFactory implements LockFallback<MyLockService> {

        @Override
        public MyLockService create(Throwable cause) {
            return new MyLockService() {
                @Override
                public String getName(String name, String age) {
                    log.error("回调cause {}", cause.getMessage());
                    throw new TryLockException("业务报错信息", cause);
                }
            };
        }
    }
}
