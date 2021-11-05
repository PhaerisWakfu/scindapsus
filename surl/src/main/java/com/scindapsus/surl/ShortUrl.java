package com.scindapsus.surl;

import lombok.Builder;
import lombok.Data;

/**
 * @author wyh
 * @date 2021/11/4 17:39
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
