package com.scindapsus.tenant.config;

import brave.Tracer;
import brave.baggage.*;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import com.scindapsus.tenant.TenantProvider;
import com.scindapsus.tenant.interceptor.TenantInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.autoconfig.SleuthBaggageProperties;
import org.springframework.cloud.sleuth.brave.propagation.PropagationFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author wyh
 * @since 1.0
 */
@ConditionalOnClass(Tracer.class)
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class TenantTraceAutoConfiguration {

    static final Log logger = LogFactory.getLog(TenantTraceAutoConfiguration.class);

    /**
     * 设置租户预定义key到sleuth中
     * 覆盖{@link org.springframework.cloud.sleuth.autoconfig.brave.BraveBaggageConfiguration#baggagePropagationFactoryBuilder}
     */
    @Bean
    BaggagePropagation.FactoryBuilder propagationFactory(PropagationFactorySupplier supplier, TenantProperties tenantProperties) {
        BaggagePropagation.FactoryBuilder factoryBuilder = BaggagePropagation.newFactoryBuilder(supplier.get());
        //添加租户预定义key到sleuth中，使其可以在baggage中传播
        factoryBuilder.add(getPropagationKey(tenantProperties.getName()));
        return factoryBuilder;
    }

    /**
     * 设置tenantName为mdc白名单
     * 覆盖{@link org.springframework.cloud.sleuth.autoconfig.brave.BraveBaggageConfiguration#correlationScopeDecoratorBuilder}
     */
    @Bean
    @ConditionalOnClass(MDC.class)
    @ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "mdc", matchIfMissing = true)
    CorrelationScopeDecorator.Builder tenantCorrelationScopeDecoratorBuilder(TenantProperties tenantProperties) {
        CorrelationScopeDecorator.Builder builder = MDCScopeDecorator.newBuilder();
        builder.add(getMDCKey(tenantProperties.getName()));
        return builder;
    }

    /**
     * 使用上一步设置过租户mdc的builder代替new出来的builder
     * 覆盖{@link org.springframework.cloud.sleuth.autoconfig.brave.BraveBaggageConfiguration#correlationScopeDecorator}
     */
    @Bean
    @ConditionalOnBean(CorrelationScopeDecorator.Builder.class)
    @ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "mdc", matchIfMissing = true)
    CurrentTraceContext.ScopeDecorator tenantCorrelationScopeDecorator(@Qualifier("spring.sleuth.log.slf4j.whitelisted-mdc-keys") List<String> whiteListedMDCKeys,
                                                                       SleuthBaggageProperties sleuthBaggageProperties,
                                                                       @Nullable List<CorrelationScopeCustomizer> correlationScopeCustomizers,
                                                                       CorrelationScopeDecorator.Builder tenantCorrelationScopeDecoratorBuilder) {

        Set<String> correlationFields = redirectOldPropertyToNew("spring.sleuth.log.slf4j.whitelisted-mdc-keys", whiteListedMDCKeys,
                "spring.sleuth.baggage.correlation-fields", sleuthBaggageProperties.getCorrelationFields());

        // Add fields from properties
        for (String field : correlationFields) {
            tenantCorrelationScopeDecoratorBuilder.add(CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageField.create(field)).build());
        }

        // handle user overrides
        if (correlationScopeCustomizers != null) {
            for (CorrelationScopeCustomizer customizer : correlationScopeCustomizers) {
                customizer.customize(tenantCorrelationScopeDecoratorBuilder);
            }
        }
        return tenantCorrelationScopeDecoratorBuilder.build();
    }

    @Bean
    @ConditionalOnMissingBean(TenantProvider.class)
    public TenantProvider defaultTenantValueProvider(TenantProperties tenantProperties) {
        return new DefaultTenantProvider(tenantProperties.getName());
    }

    @Bean
    @ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "propagation.enabled",
            havingValue = "true")
    public TenantProviderConfigurer tenantValueConfigurer(TenantProvider tenantProvider, TenantProperties tenantProperties) {
        return new TenantProviderConfigurer(tenantProvider, tenantProperties.getPropagation());
    }


    /**
     * refer{@link org.springframework.cloud.sleuth.autoconfig.brave.BraveBaggageConfiguration#redirectOldPropertyToNew}
     */
    @SuppressWarnings("all")
    static Set<String> redirectOldPropertyToNew(String oldProperty, List<String> oldValue, String newProperty,
                                                List<String> newValue) {
        Set<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        result.addAll(newValue);
        if (!oldValue.isEmpty()) {
            logger.warn("'" + oldProperty + "' has been renamed to '" + newProperty
                    + "' and will be removed in a future release.");
            result.addAll(oldValue); // dedupes
        }
        return result;
    }

    /**
     * 设置传播key
     *
     * @param propagationKey 传播key名
     * @return 传播key
     */
    private BaggagePropagationConfig getPropagationKey(String propagationKey) {
        return BaggagePropagationConfig.SingleBaggageField.remote(BaggageField.create(propagationKey));
    }

    /**
     * 设置mdc白名单key
     *
     * @param mdcKey 允许放入mdc的key
     * @return mdcKey
     */
    private CorrelationScopeConfig getMDCKey(String mdcKey) {
        return CorrelationScopeConfig.SingleCorrelationField.newBuilder(BaggageField.create(mdcKey)).build();
    }


    /**
     * 默认从header中取配置的tenantName
     */
    public static class DefaultTenantProvider implements TenantProvider {

        private final String tenantName;

        public DefaultTenantProvider(String tenantName) {
            this.tenantName = tenantName;
        }

        @Override
        public String getTenantValue(HttpServletRequest request) {
            return request.getHeader(tenantName);
        }
    }

    /**
     * 配置租户拦截器
     */
    public static class TenantProviderConfigurer implements WebMvcConfigurer {

        private final TenantProvider tenantProvider;

        private final TenantProperties.Propagation propagation;

        public TenantProviderConfigurer(TenantProvider tenantProvider, TenantProperties.Propagation propagation) {
            this.tenantProvider = tenantProvider;
            this.propagation = propagation;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new TenantInterceptor(tenantProvider))
                    .addPathPatterns(propagation.getPatterns());
        }
    }
}
