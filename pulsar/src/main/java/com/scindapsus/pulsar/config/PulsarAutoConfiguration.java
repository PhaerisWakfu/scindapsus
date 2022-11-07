package com.scindapsus.pulsar.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.scindapsus.pulsar.PulsarAuthenticationProvider;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author wyh
 * @since 2022/11/1
 */
@Configuration
@EnableConfigurationProperties(PulsarProperties.class)
class PulsarAutoConfiguration {

    private static final String[] IGNORE_FIELDS = {"clock"};

    @Bean
    PulsarClient pulsarClient(PulsarProperties properties) throws PulsarClientException {

        PulsarProperties.Client clientProperties = properties.getClient();

        //loadConf
        Map<String, Object> configMap = BeanUtil.beanToMap(clientProperties
                .getProperty(), false, true);
        Arrays.stream(IGNORE_FIELDS).forEach(configMap::remove);

        //builder
        ClientBuilder clientBuilder = PulsarClient.builder().loadConf(configMap);

        //authentication
        Optional.ofNullable(clientProperties.getAuthClassName()).ifPresent(authClassName -> {
            try {
                clientBuilder.authentication(ReflectUtil
                        .<PulsarAuthenticationProvider>newInstance(authClassName)
                        .get());
            } catch (Exception e) {
                throw new PulsarConfigException("schema class is not found", e);
            }
        });
        return clientBuilder.build();
    }
}
