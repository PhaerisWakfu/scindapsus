package com.scindapsus.log.desensitize;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import cn.hutool.core.util.DesensitizedUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 敏感信息数据转换器(目前仅支持整行日志是json格式的情况脱敏)
 *
 * @author wyh
 * @date 2020年12月23日 16:59:25
 */
public class LogDesensitizationConverter extends MessageConverter {

    /**
     * 日志脱敏开关
     */
    private String allowRun;

    /**
     * 日志脱敏关键字
     */
    private List<DesensitizeRule> desensitizeRules;


    private static final String ENABLED_KEY = "DesensitizeEnabled";
    private static final String ENABLED = "true";
    private static final String CLOSED = "false";
    private static final String SENSITIVE_KEYS = "SensitiveDataKeys";

    @Override
    public String convert(ILoggingEvent event) {
        // 从xml中获取定义的开关与脱敏关键字
        if (allowRun == null) {
            Context context = getContext();
            String isEnabled = context.getProperty(ENABLED_KEY);
            allowRun = StringUtils.hasText(isEnabled) ? isEnabled : CLOSED;
            try {
                desensitizeRules = JSON.parseArray(context.getProperty(SENSITIVE_KEYS), DesensitizeRule.class);
            } catch (Exception e) {
                desensitizeRules = null;
            }
        }

        // 获取原始日志
        String originalMessage = event.getFormattedMessage();

        // 缺少必要参数不脱敏
        if (!ENABLED.equals(allowRun) || CollectionUtils.isEmpty(desensitizeRules) || !StringUtils.hasText(originalMessage)) {
            return originalMessage;
        }

        // 日志脱敏
        return filterMessage(originalMessage);
    }

    /**
     * 处理日志字符串，返回脱敏后的字符串
     *
     * @param message 原始日志字符串
     * @return 脱敏后的字符串
     */
    public String filterMessage(String message) {
        Object json;
        try {
            json = JSON.parse(message);
        } catch (Exception e) {
            //如果不是json返回原始字符串
            return message;
        }
        //开始脱敏
        for (DesensitizeRule desensitizeRule : desensitizeRules) {
            desensitize(json, desensitizeRule);
        }
        return json.toString();
    }

    /**
     * 脱敏
     *
     * @param obj             需要脱敏的对象
     * @param desensitizeRule 脱敏规则
     */
    private static void desensitize(Object obj, DesensitizeRule desensitizeRule) {
        if (obj == null) return;
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            for (Object ele : jsonArray) {
                desensitize(ele, desensitizeRule);
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.forEach((k, v) -> {
                if (desensitizeRule.getFieldName().equals(k) && !(v instanceof JSON)) {
                    jsonObject.put(k, DesensitizedUtil.desensitized(v.toString(), desensitizeRule.getFormat()));
                } else {
                    desensitize(v, desensitizeRule);
                }
            });
        }
    }
}
