package com.scindapsus.log.config;

import com.scindapsus.log.aspect.LogAspect;
import com.scindapsus.log.trace.ScindapsusTracer;
import com.scindapsus.log.user.UserProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyh
 * @date 2021/10/9 14:19
 */
@Configuration
public class LogConfiguration {

    @Bean
    public LogAspect logAspect(ObjectProvider<ScindapsusTracer> tracer, ObjectProvider<UserProvider<?>> user) {
        return new LogAspect(tracer.getIfAvailable(), user.getIfAvailable());
    }
}
