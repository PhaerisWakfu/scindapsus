package com.scindapsus.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wyh
 * @date 2021/10/9 10:22
 */
@Getter
@Setter
@Builder
public class LogBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBase.class);

    /**
     * 请求路径
     */
    @JSONField
    private String path;

    /**
     * 事件名称,一般就是业务方法名称
     */
    @JSONField(ordinal = 1)
    private String eventName;

    /**
     * 调用链id
     */
    @JSONField(ordinal = 2)
    private String traceId;

    /**
     * C端用户id
     */
    @JSONField(ordinal = 3)
    private Object userId;

    /**
     * 接口响应时间
     */
    @JSONField(ordinal = 4)
    private long costTime;

    /**
     * 接口请求入参
     */
    @JSONField(ordinal = 5)
    private Object request;

    /**
     * 接口返回值
     */
    @JSONField(ordinal = 6)
    private Object response;

    /**
     * 其他业务参数
     */
    @JSONField(ordinal = 7)
    private Object others;


    /**
     * 日志打印
     */
    public void print() {
        debugPrint();
    }

    /**
     * 用debug日志打印
     */
    public void debugPrint() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JSON.toJSONString(this));
        }
    }

    /**
     * 用info日志打印
     */
    public void infoPrint() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(JSON.toJSONString(this));
        }
    }

    /**
     * 用warn日志打印
     */
    public void warnPrint() {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(JSON.toJSONString(this));
        }
    }

    /**
     * 用error日志打印
     */
    public void errorPrint() {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(JSON.toJSONString(this));
        }
    }
}
