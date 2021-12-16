package com.scindapsus.surl;

import lombok.Builder;
import lombok.Data;

/**
 * @author wyh
 * @since 1.0
 */
@Data
@Builder
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
