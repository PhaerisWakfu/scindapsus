package com.scindapsus.log.aspect;

import com.scindapsus.log.LogBase;
import com.scindapsus.log.trace.TracerProvider;
import com.scindapsus.log.user.UserProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 请求日志打印切面
 *
 * @author wyh
 * @since 1.0
 */
@Aspect
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final TracerProvider tracer;

    private final UserProvider<?> user;

    public LogAspect(TracerProvider tracer, UserProvider<?> user) {
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
            LogBase.getInstance()
                    .setPath(Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()))
                            .map(x -> x.getRequest().getServletPath())
                            .orElse(null))
                    .setEventName(point.getSignature().toShortString())
                    .setCostTime(stopWatch.getTotalTimeMillis())
                    .setRequest(point.getArgs())
                    .setResponse(result)
                    .setTraceId(Optional.ofNullable(tracer).map(TracerProvider::getTraceId).orElse(null))
                    .setUserId(Optional.ofNullable(user).map(UserProvider::getUserId).orElse(null))
                    .print();
        } catch (Exception e) {
            //抛错后只打印日志，避免影响到业务逻辑
            log.warn("recording operation log failed", e);
        }
        return result;
    }
}
