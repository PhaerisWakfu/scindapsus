package com.scindapsus.log.desensitize;

import cn.hutool.core.util.DesensitizedUtil;

/**
 * @author wyh
 * @since 1.0
 */
public class DesensitizeRule {

    /**
     * 需要脱敏的字段
     */
    private String fieldName;

    /**
     * 脱敏格式化规则
     */
    private DesensitizedUtil.DesensitizedType format;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DesensitizedUtil.DesensitizedType getFormat() {
        return format;
    }

    public void setFormat(DesensitizedUtil.DesensitizedType format) {
        this.format = format;
    }
}
