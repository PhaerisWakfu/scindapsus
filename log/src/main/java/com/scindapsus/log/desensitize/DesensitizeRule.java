package com.scindapsus.log.desensitize;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;

/**
 * @author wyh
 * @since 1.0
 */
@Data
public class DesensitizeRule {

    /**
     * 需要脱敏的字段
     */
    private String fieldName;

    /**
     * 脱敏格式化规则
     */
    private DesensitizedUtil.DesensitizedType format;
}
