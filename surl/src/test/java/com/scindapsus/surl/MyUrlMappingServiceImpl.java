package com.scindapsus.surl;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wyh
 * @since 2021/11/5
 */
@Component
public class MyUrlMappingServiceImpl implements UrlMappingService {

    private static final Map<String, String> myMapping = new ConcurrentHashMap<>();

    @Override
    public void save(ShortUrl shortUrl) {
        myMapping.put(shortUrl.getKey(), shortUrl.getOriginalUrl());
    }

    @Override
    public String get(String key) {
        return myMapping.get(key);
    }
}
