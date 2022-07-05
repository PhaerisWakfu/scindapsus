package com.scindapsus.ds.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wyh
 * @date 2022/7/4 16:13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DSConstants {

    /**
     * 数据源bean名称后缀
     */
    public static final String DS_NAME_SUFFIX = "_datasource";

    /**
     * 默认数据源的key
     */
    public static final String DEFAULT_ROUTING_KEY = "default";
}
