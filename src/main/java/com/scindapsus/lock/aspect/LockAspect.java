package com.scindapsus.lock.aspect;

import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.annotation.DistributedLock;
import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.KeyPrefixGenerator;
import com.scindapsus.lock.support.SpringExpressionLangParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * distributed lock aspect
 *
 * @author wyh
 * @since 1.0
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class LockAspect {

    private final LockRegistryFactory<?> lockRegistryFactory;

    private final KeyPrefixGenerator keyPrefixGenerator;

    /**
     * try lock
     *
     * @param point 切点
     * @param lock  分布式琐注解
     * @return 切点方法返回结果
     * @throws Throwable
     */
    @Around("@annotation(lock)")
    public Object doAroundAdvice(ProceedingJoinPoint point, DistributedLock lock) {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Object target = point.getTarget();
        Object[] args = point.getArgs();

        String name = lock.name();
        String key = lock.key();
        long expire = lock.expire();
        long retryDuration = lock.retryDuration();
        Class<?> callback = lock.fallback();

        //生成实际锁名称
        String lockName = this.generateLockName(name, key, methodSignature, target, args);

        //生成工厂对应的lockRegistry实例
        LockRegistry lockRegistry = lockRegistryFactory.generate(expire);

        boolean locked = false;
        Lock obtain = lockRegistry.obtain(lockName);
        try {
            //加锁
            locked = retryDuration > 0
                    ? obtain.tryLock(retryDuration, TimeUnit.MILLISECONDS)
                    : obtain.tryLock();
            log.info("end of lock, lockName: [{}], locked: [{}]", lockName, locked);

            //加锁失败回调
            if (!locked) {
                this.invokeFallback(callback, methodSignature, args);
                return null;
            }

            //继续执行业务逻辑
            return point.proceed();
        } catch (Throwable e) {
            //失败回调
            this.invokeFallback(callback, methodSignature, args);
            throw new DistributedLockException("try lock proceed failed", e);
        } finally {
            if (locked) {
                try {
                    obtain.unlock();
                    log.info("release lock: [{}]", lockName);
                } catch (Exception e) {
                    log.warn("release lock failed", e);
                }
            }
        }
    }


    /**
     * invoke client provide same method
     *
     * @param fallback        回调类
     * @param methodSignature 方法签名
     */
    private void invokeFallback(Class<?> fallback, MethodSignature methodSignature, Object[] args) {
        if (fallback != void.class) {
            try {
                Method method = fallback.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
                method.invoke(fallback.newInstance(), args);
            } catch (NoSuchMethodException e) {
                throw new DistributedLockException("cannot find fallback method", e);
            } catch (InstantiationException e) {
                throw new DistributedLockException("fallback method must be have a empty construct", e);
            } catch (IllegalAccessException e) {
                throw new DistributedLockException("fallback method must be public", e);
            } catch (InvocationTargetException e) {
                throw new DistributedLockException("invoke fallback failed", e);
            }
        }
    }

    /**
     * generate lock name
     *
     * @param inputName       注解中的name
     * @param inputKey        注解中的key
     * @param methodSignature 方法签名
     * @param target          method所在的对象
     * @param args            入参
     * @return lock name
     * @throws DistributedLockException
     */
    private String generateLockName(String inputName, String inputKey, MethodSignature methodSignature,
                                    Object target, Object[] args) {
        String name = null;
        if (!ObjectUtils.isEmpty(inputName)) {
            name = parseWithExpression(inputName, methodSignature, target, args);
        }

        String key = ObjectUtils.isEmpty(inputKey)
                ? defaultLockKey(methodSignature)
                : parseWithExpression(inputKey, methodSignature, target, args);

        return keyPrefixGenerator.compute(name, key);
    }

    /**
     * parse from spring expression language
     *
     * @param expression      表达式
     * @param methodSignature 方法签名
     * @param target          method所在的对象
     * @param args            入参
     * @return spring expression language explain
     * @throws DistributedLockException
     */
    private String parseWithExpression(String expression, MethodSignature methodSignature, Object target, Object[] args) {
        String parse = SpringExpressionLangParser.parse(target, expression, methodSignature.getMethod(), args);
        log.info("expression: [{}], parse result: [{}]", expression, parse);
        return Optional.ofNullable(parse).orElseThrow(() -> new DistributedLockException("explain expression failed"));
    }

    /**
     * 获取默认的key
     *
     * @param methodSignature 方法签名
     * @return lock key
     */
    private String defaultLockKey(MethodSignature methodSignature) {
        return methodSignature.toString();
    }
}
