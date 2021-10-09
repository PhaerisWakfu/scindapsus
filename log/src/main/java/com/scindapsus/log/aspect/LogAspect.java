package com.scindapsus.log.aspect;

import com.scindapsus.log.LogBase;
import com.scindapsus.log.trace.ScindapsusTracer;
import com.scindapsus.log.user.UserProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author wyh
 * @date 2021/10/9 10:49
 */
@Slf4j
@Aspect
@AllArgsConstructor
public class LogAspect {

    private final ScindapsusTracer tracer;

    private final UserProvider<?> user;


    @Around("@annotation(com.scindapsus.log.annotation.OPLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //执行业务
        Object result = point.proceed();
        stopWatch.stop();
        try {
            LogBase.builder()
                    .path(Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                            .map(x -> x.getRequest().getServletPath())
                            .orElse(null))
                    .eventName(point.getSignature().toShortString())
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
