package com.scindapsus.log;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wyh
 * @date 2021/10/9 10:22
 */
@Getter
@Setter
@Builder
public class LogBase {

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
     * 结果消息
     */
    @JsonProperty(index = 3)
    private String msg;

    /**
     * 接口响应时间
     */
    @JsonProperty(index = 4)
    private long costTime;

    /**
     * C端用户id
     */
    @JsonProperty(index = 6)
    private Object userId;

    /**
     * 其他业务参数
     */
    @JsonProperty(index = 7)
    private Object others;

    /**
     * 接口请求入参
     */
    @JsonProperty(index = 8)
    private Object request;

    /**
     * 接口返回值
     */
    @JsonProperty(index = 9)
    private Object response;


    /**
     * 用debug日志打印自己
     */
    public void print() {
        LogTemplate.debug(this);
    }

    /**
     * 用info日志打印自己
     */
    public void infoPrint() {
        LogTemplate.debug(this);
    }

    /**
     * 用warn日志打印自己
     */
    public void warnPrint() {
        LogTemplate.debug(this);
    }

    /**
     * 用error日志打印自己
     */
    public void errorPrint() {
        LogTemplate.debug(this);
    }
}
