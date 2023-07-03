package com.scindapsus.surl;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author wyh
 * @since 2021/11/5
 */
@Getter
@Setter
@Accessors(chain = true)
public class ShortUrl {

    /**
     * 原始URL转成的短链key
     */
    private String key;

    /**
     * 原始URL
     */
    private String originalUrl;
}
