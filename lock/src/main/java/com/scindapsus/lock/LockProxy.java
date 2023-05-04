package com.scindapsus.lock;

import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.SpringExpressionLangParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 动态代理加锁
 *
 * @author wyh
 * @since 2021/10/9
 */
public class LockProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(LockRegistryFactoryHolder.class);

    private final Object target;

    private final String express;

    private final Duration retryDuration;


    public LockProxy(Object target, String express, Duration retryDuration) {
        this.target = target;
        this.express = express;
        this.retryDuration = retryDuration;
    }


    /**
     * jdk动态代理方法加锁，只适用于类实现了interface的方法的代理
     *
     * @param target  实现类
     * @param express 琐key表达式（支持spel）
     * @param <T>     实现类类型
     * @return 代理后的类
     */
    public static <T> T proxy(T target, String express) {
        return proxy(target, express, null);
    }

    /**
     * jdk动态代理方法加锁，只适用于类实现了interface的方法的代理
     *
     * @param target        实现类
     * @param express       琐key表达式（支持spel）
     * @param retryDuration 尝试获取琐多少时间,获取不到抛{@link DistributedLockException}
     * @param <T>           实现类类型
     * @return 代理后的类
     */
    @SuppressWarnings("unchecked")
    public static <T> T proxy(T target, String express, Duration retryDuration) {
        if (target == null || express == null) {
            return target;
        }
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LockProxy(target, express, retryDuration));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //解析表达式
        String key = SpringExpressionLangParser.parse(target, express, method, args);
        //获取琐对象
        Lock lock = LockRegistryFactoryHolder.getLock().obtain(key);
        return Optional.ofNullable(retryDuration)
                .map(r -> tryLock(lock, method, args, r))
                .orElseGet(() -> lock(lock, method, args));
    }

    private Object lock(Lock lock, Method method, Object[] args) {
        //阻塞式获取琐
        lock.lock();
        try {
            log.debug("---get lock succeed");
            return method.invoke(target, args);
        } catch (Exception e) {
            log.error("try lock exception ->", e);
            throw new DistributedLockException("get lock failed");
        } finally {
            lock.unlock();
            log.debug("---release lock succeed");
        }
    }

    private Object tryLock(Lock lock, Method method, Object[] args, Duration retryDuration) {
        boolean got = false;
        try {
            if (got = lock.tryLock(retryDuration.toMillis(), TimeUnit.MILLISECONDS)) {
                log.debug("---try lock succeed");
                return method.invoke(target, args);
            }
        } catch (Exception e) {
            log.error("try lock exception ->", e);
        } finally {
            if (got) {
                lock.unlock();
                log.debug("---release lock succeed");
            }
        }
        throw new DistributedLockException("get lock failed");
    }
}