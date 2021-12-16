package com.scindapsus.log.config;

import com.scindapsus.log.aspect.LogAspect;
import com.scindapsus.log.trace.TracerProvider;
import com.scindapsus.log.user.UserProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

/**
 * @author wyh
 * @since 1.0
 */
public class LogConfiguration {

    /**
     * 注册日志打印切面
     *
     * @param tracer 调用链provider
     * @param user   用户provider
     * @return 日志打印切面
     */
    @Bean
    public LogAspect logAspect(ObjectProvider<TracerProvider> tracer, ObjectProvider<UserProvider<?>> user) {
        return new LogAspect(tracer.getIfAvailable(), user.getIfAvailable());
    }
}
