package com.scindapsus.log.desensitize;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.Data;

/**
 * @author wyh
 * @date 2021/10/11 14:28
 */
@Data
public class DesensitizeRule {

    private String fieldName;

    private DesensitizedUtil.DesensitizedType format;
}
