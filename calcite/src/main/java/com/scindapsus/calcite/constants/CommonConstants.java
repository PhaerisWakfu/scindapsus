package com.scindapsus.calcite.constants;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wyh
 * @since 2023/4/24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstants {

    public static final String CONFIG_PATH = "calcite.json";

    public static final String TEMP_PATH = "templates/calcite.stg";

    public static final String ST_NAME_GET_CONFIG = "getConfig";

    public static final String ST_ARG_SCHEMAS = "schemas";

    public static final String ST_ARG_DEFAULT = "def";
}
