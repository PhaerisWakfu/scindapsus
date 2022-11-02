package com.scindapsus.pulsar;

import org.apache.pulsar.client.api.Authentication;
import org.apache.pulsar.client.api.AuthenticationFactory;

/**
 * @author wyh
 * @since 2022/11/2
 */
public class MyAuthenticationProvider implements PulsarAuthenticationProvider {
    @Override
    public Authentication get() {
        return AuthenticationFactory.token("xxx");
    }
}
