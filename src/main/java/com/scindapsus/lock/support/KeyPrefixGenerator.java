package com.scindapsus.lock.support;


import java.util.Optional;

/**
 * @author wyh
 * @since 1.0
 */
public class KeyPrefixGenerator {

    private static final String SEPARATOR = ":";

    private static final String EMPTY_STR = "";


    public String compute(String region, String key) {
        String regionName = Optional.ofNullable(region).map(x -> x + SEPARATOR).orElse(EMPTY_STR);
        return regionName + key;
    }
}
