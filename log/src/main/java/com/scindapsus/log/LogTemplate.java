package com.scindapsus.log;

import com.scindapsus.log.util.JacksonUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wyh
 * @date 2021/10/9 10:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTemplate.class);


    /**
     * 打印固定格式的debug日志
     *
     * @param msg 日志内容
     */
    public static void debug(LogBase msg) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JacksonUtil.toJSONString(msg));
        }
    }

    /**
     * 打印固定格式的info日志
     *
     * @param msg 日志内容
     */
    public static void info(LogBase msg) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(JacksonUtil.toJSONString(msg));
        }
    }

    /**
     * 打印固定格式的warn日志
     *
     * @param msg 日志内容
     */
    public static void warn(LogBase msg) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(JacksonUtil.toJSONString(msg));
        }
    }

    /**
     * 打印固定格式的error日志
     *
     * @param msg 日志内容
     */
    public static void error(LogBase msg) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(JacksonUtil.toJSONString(msg));
        }
    }
}
