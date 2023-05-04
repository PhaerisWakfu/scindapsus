package com.scindapsus.ds.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * 改造于baomidou的多数据源本地事务方案<a href="https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter">dynamic-datasource</a>
 *
 * @author funkye
 * @author wyh
 * @since 2022/7/4
 */
public class TxUtil {

    private static final ThreadLocal<String> TX_ID_THREAD_LOCAL = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(TxUtil.class);


    private TxUtil() {
    }

    /**
     * 开始一个事务
     */
    public static String begin() {
        String txId = Optional.ofNullable(getTxId()).orElseGet(() -> {
            String newId = UUID.randomUUID().toString();
            setTxId(newId);
            LOGGER.debug("[{}] ===> tx create", newId);
            return newId;
        });
        LOGGER.debug("[{}] ===> tx exists", txId);
        return txId;
    }

    /**
     * 提交事务
     */
    public static void commit() throws Exception {
        try {
            ConnectionFactory.notify(true);
        } finally {
            LOGGER.debug("[{}] ===> tx commit", getTxId());
            remove();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback() throws Exception {
        try {
            ConnectionFactory.notify(false);
        } finally {
            LOGGER.debug("[{}] ===> tx rollback", getTxId());
            remove();
        }
    }

    /**
     * 生成事务ID
     */
    public static String getTxId() {
        String currentTxId = TX_ID_THREAD_LOCAL.get();
        if (StringUtils.hasText(currentTxId)) {
            return currentTxId;
        }
        return null;
    }

    /**
     * 设置事务ID
     */
    private static void setTxId(String txId) {
        TX_ID_THREAD_LOCAL.set(txId);
    }

    /**
     * 清空threadLocal
     */
    private static void remove() {
        TX_ID_THREAD_LOCAL.remove();
    }
}
