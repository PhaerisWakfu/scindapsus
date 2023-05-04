package com.scindapsus.lock.aspect;

import com.scindapsus.lock.LockFallback;
import com.scindapsus.lock.LockKeyPrefixGenerator;
import com.scindapsus.lock.LockRegistryFactory;
import com.scindapsus.lock.annotation.DistributedLock;
import com.scindapsus.lock.exception.DistributedLockException;
import com.scindapsus.lock.support.SpringExpressionLangParser;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * distributed lock aspect
 *
 * @author wyh
 * @since 2021/10/9
 */
@Aspect
public class LockAspect {

    private static final Logger log = LoggerFactory.getLogger(LockAspect.class);

    private final LockRegistryFactory lockRegistryFactory;

    private final LockKeyPrefixGenerator lockKeyPrefixGenerator;

    private final ApplicationContext applicationContext;

    public LockAspect(LockRegistryFactory lockRegistryFactory, LockKeyPrefixGenerator lockKeyPrefixGenerator,
                      ApplicationContext applicationContext) {
        this.lockRegistryFactory = lockRegistryFactory;
        this.lockKeyPrefixGenerator = lockKeyPrefixGenerator;
        this.applicationContext = applicationContext;
    }

    /**
     * try lock
     *
     * @param point 切点
     * @param lock  分布式琐注解
     * @return 切点方法返回结果
     */
    @Around("@annotation(lock)")
    public Object doAroundAdvice(ProceedingJoinPoint point, DistributedLock lock) {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Object target = point.getTarget();
        Object[] args = point.getArgs();

        String name = lock.name();
        String[] key = lock.key();
        long retryDuration = lock.retryDuration();
        Class<? extends LockFallback<?>> fallback = lock.fallback();

        //生成实际锁名称
        String lockName = this.generateLockName(name, key, methodSignature, target, args);
        //是否加锁成功
        boolean locked = false;
        //生成工厂对应的lockRegistry实例
        LockRegistry lockRegistry = lockRegistryFactory.generate();
        Lock obtain = lockRegistry.obtain(lockName);
        try {
            //加锁
            locked = retryDuration > 0
                    ? obtain.tryLock(retryDuration, TimeUnit.MILLISECONDS)
                    : obtain.tryLock();
            log.debug("Try lock [{}ms], lockName: [{}], locked: [{}].", retryDuration > 0 ? retryDuration : 0, lockName, locked);
            //加锁失败抛错
            if (!locked) {
                throw new DistributedLockException("Try lock failed.");
            }
            //继续执行业务逻辑
            return point.proceed();
        } catch (Throwable e) {
            //报错回调
            return invokeFallback(fallback, methodSignature, args, e);
        } finally {
            if (locked) {
                try {
                    obtain.unlock();
                    log.debug("Release lock: [{}].", lockName);
                } catch (Exception e) {
                    log.debug("Release lock failed.", e);
                }
            }
        }
    }

    /**
     * invoke client provide same method
     *
     * @param fallback        回调类
     * @param methodSignature 方法签名
     * @param args            方法参数
     * @param throwable       加锁报错异常(如有)
     */
    private Object invokeFallback(Class<? extends LockFallback<?>> fallback, MethodSignature methodSignature, Object[] args,
                                  Throwable throwable) {
        try {
            //获取失败回调工厂
            LockFallback<?> target = applicationContext.getBean(fallback);
            //获取失败回调类
            Object create = target.create(throwable);
            //调用回调方法
            Method method = create.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            method.setAccessible(true);
            return method.invoke(create, args);
        } catch (NoSuchMethodException e) {
            throw new DistributedLockException("Cannot find fallback method.", e);
        } catch (IllegalAccessException e) {
            throw new DistributedLockException("Fallback method must be public.", e);
        } catch (InvocationTargetException e) {
            throw new DistributedLockException("Invoke fallback exception.", e);
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
     */
    private String generateLockName(String inputName, String[] inputKey, MethodSignature methodSignature,
                                    Object target, Object[] args) {
        String name = null;
        if (!ObjectUtils.isEmpty(inputName)) {
            name = explainWithExpression(inputName, methodSignature, target, args);
        }
        String[] key = {defaultLockKey(methodSignature)};
        if (ArrayUtils.isNotEmpty(inputKey)) {
            key = Arrays.stream(inputKey)
                    .map(k -> explainWithExpression(k, methodSignature, target, args))
                    .toArray(String[]::new);
        }
        return lockKeyPrefixGenerator.compute(name, key);
    }

    /**
     * explain from spring expression language
     *
     * @param expression      表达式
     * @param methodSignature 方法签名
     * @param target          method所在的对象
     * @param args            入参
     * @return spring expression language explain
     */
    private String explainWithExpression(String expression, MethodSignature methodSignature, Object target, Object[] args) {
        String parse = SpringExpressionLangParser.parse(target, expression, methodSignature.getMethod(), args);
        log.debug("Lock expression: [{}], explain result: [{}].", expression, parse);
        return Optional.ofNullable(parse).orElseThrow(() -> new DistributedLockException("Explain expression failed."));
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
