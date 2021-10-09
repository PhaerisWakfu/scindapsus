package com.scindapsus.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scindapsus.log.util.JacksonUtil;
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
     * 事件名称,一般就是业务方法名称
     */
    @JsonProperty(index = 1)
    private String eventName;

    /**
     * 调用链id
     */
    @JsonProperty(index = 2)
    private String traceId;

    /**
     * C端用户id
     */
    @JsonProperty(index = 3)
    private Object userId;

    /**
     * 接口响应时间
     */
    @JsonProperty(index = 4)
    private long costTime;

    /**
     * 接口请求入参
     */
    @JsonProperty(index = 5)
    private Object request;

    /**
     * 接口返回值
     */
    @JsonProperty(index = 6)
    private Object response;

    /**
     * 其他业务参数
     */
    @JsonProperty(index = 7)
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
            LOGGER.debug(JacksonUtil.toJSONString(this));
        }
    }

    /**
     * 用info日志打印
     */
    public void infoPrint() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(JacksonUtil.toJSONString(this));
        }
    }

    /**
     * 用warn日志打印
     */
    public void warnPrint() {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(JacksonUtil.toJSONString(this));
        }
    }

    /**
     * 用error日志打印
     */
    public void errorPrint() {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(JacksonUtil.toJSONString(this));
        }
    }
}
