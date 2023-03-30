package com.scindapsus.lock;

import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.SpringExpressionLangParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

/**
 * 动态代理加锁
 *
 * @author wyh
 * @since 1.0
 */
public class LockProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(LockRegistryFactoryHolder.class);

    private final Object target;

    private final String express;

    private final Object orElse;

    public LockProxy(Object target, String express, Object orElse) {
        this.target = target;
        this.express = express;
        this.orElse = orElse;
    }


    /**
     * jdk动态代理方法加锁，只适用于类实现了interface的方法的代理
     * <p>
     * 加锁失败抛{@link com.scindapsus.lock.exception.DistributedLockException}
     *
     * @param target  实现类
     * @param express 琐key表达式（支持spring expression lang）
     * @param <T>     实现类类型
     * @return 代理后的类
     */
    public static <T> T proxy(T target, String express) {
        return proxy(target, express, null);
    }

    /**
     * jdk动态代理方法加锁，只适用于类实现了interface的方法的代理
     *
     * @param target  实现类
     * @param express 琐key表达式（支持spring expression lang）
     * @param orElse  如果加锁失败返回什么结果,为null则抛错{@link com.scindapsus.lock.exception.DistributedLockException}
     * @param <T>     实现类类型
     * @return 代理后的类
     */
    @SuppressWarnings("unchecked")
    public static <T> T proxy(T target, String express, @Nullable Object orElse) {
        if (target == null || express == null) {
            return target;
        }
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LockProxy(target, express, orElse));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //解析表达式
        String key = SpringExpressionLangParser.parse(target, express, method, args);
        //获取琐对象
        Lock lock = LockRegistryFactoryHolder.getLock().obtain(key);
        boolean got = false;
        try {
            if (got = lock.tryLock()) {
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
        return Optional.ofNullable(orElse)
                .orElseThrow(() -> new DistributedLockException("try lock failed"));
    }
}