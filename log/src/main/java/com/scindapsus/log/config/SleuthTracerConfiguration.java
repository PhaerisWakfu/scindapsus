package com.scindapsus.log.config;

import brave.Tracer;
import com.scindapsus.log.trace.ScindapsusTracer;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyh
 * @date 2021/10/9 14:03
 */
@Configuration
@ConditionalOnClass(Tracer.class)
public class SleuthTracerConfiguration {

    @Bean
    public ScindapsusTracer scindapsusTracer(Tracer tracer) {
        return new SleuthTracer(tracer);
    }


    @AllArgsConstructor
    public static class SleuthTracer implements ScindapsusTracer {

        private final Tracer tracer;

        @Override
        public String getTraceId() {
            return tracer.nextSpan().context().traceIdString();
        }
    }
}
