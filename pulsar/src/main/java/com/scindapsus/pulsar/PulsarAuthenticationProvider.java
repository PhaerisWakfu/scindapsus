package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.Authentication;

/**
 * @author wyh
 * @since 2022/11/2
 */
public interface PulsarAuthenticationProvider {

    /**
     * 设置broker认证
     *
     * @return {@link Authentication}
     */
    Authentication get();
}
