package com.scindapsus.log.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author wyh
 * @date 2021/10/9 10:42
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @SneakyThrows
    public static String toJSONString(Object obj) {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }
}
