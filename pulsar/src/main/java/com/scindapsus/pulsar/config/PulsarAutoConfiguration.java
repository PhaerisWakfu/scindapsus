package com.scindapsus.pulsar.config;

import cn.hutool.core.bean.BeanUtil;
import com.scindapsus.pulsar.PulsarAuthenticationProvider;
import com.scindapsus.pulsar.exception.PulsarConfigException;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

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

        //authentication
        PulsarAuthenticationProvider authenticationProvider;
        try {
            authenticationProvider = (PulsarAuthenticationProvider) Class.forName(clientProperties.getAuthClassName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new PulsarConfigException("schema class is not found");
        }

        return PulsarClient.builder()
                .loadConf(configMap)
                .authentication(authenticationProvider.get())
                .build();
    }
}
