package com.scindapsus.surl;


/**
 * @author wyh
 * @since 1.0
 */
public class ShortUrl {

    /**
     * 原始URL转成的短链key
     */
    private String key;

    /**
     * 原始URL
     */
    private String originalUrl;


    public static ShortUrl getInstance() {
        return new ShortUrl();
    }

    public String getKey() {
        return key;
    }

    public ShortUrl setKey(String key) {
        this.key = key;
        return this;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public ShortUrl setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        return this;
    }
}
