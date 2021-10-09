package com.scindapsus.log.aspect;

import com.scindapsus.log.LogBase;
import com.scindapsus.log.trace.ScindapsusTracer;
import com.scindapsus.log.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author wyh
 * @date 2021/10/9 10:49
 */
@Slf4j
@Aspect
public class LogAspect {

    private final ScindapsusTracer tracer;

    private final UserProvider<?> user;

    public LogAspect(ScindapsusTracer tracer, UserProvider user) {
        this.tracer = tracer;
        this.user = user;
    }

    @Around("@annotation(com.scindapsus.log.annotation.OPLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //执行业务
        Object result = point.proceed();
        stopWatch.stop();
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method currentMethod = point.getTarget().getClass().getMethod(signature.getName(), signature.getParameterTypes());
            LogBase.builder()
                    .eventName(currentMethod.getName())
                    .costTime(stopWatch.getTotalTimeMillis())
                    .request(point.getArgs())
                    .response(result)
                    .traceId(Optional.ofNullable(tracer).map(ScindapsusTracer::getTraceId).orElse(null))
                    .userId(Optional.ofNullable(user).map(UserProvider::getUserId).orElse(null))
                    .build()
                    .print();
        } catch (Exception e) {
            //抛错后只打印日志，避免影响到业务逻辑
            log.warn("recording operation log failed", e);
        }
        return result;
    }
}
