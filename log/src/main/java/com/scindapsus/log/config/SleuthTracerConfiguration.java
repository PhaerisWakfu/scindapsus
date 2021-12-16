package com.scindapsus.log.config;

import brave.Tracer;
import com.scindapsus.log.trace.TracerProvider;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 引入spring-cloud-starter-sleuth生效
 *
 * @author wyh
 * @since 1.0
 */
@Configuration
@ConditionalOnClass(Tracer.class)
public class SleuthTracerConfiguration {

    /**
     * 默认调用链provider
     *
     * @param tracer brave.tracer
     * @return 调用链provider
     */
    @Bean
    @ConditionalOnMissingBean
    public TracerProvider scindapsusTracer(Tracer tracer) {
        return new SleuthTracer(tracer);
    }


    /**
     * spring cloud sleuth tracer
     */
    @AllArgsConstructor
    public static class SleuthTracer implements TracerProvider {

        private final Tracer tracer;

        @Override
        public String getTraceId() {
            return tracer.nextSpan().context().traceIdString();
        }
    }
}
