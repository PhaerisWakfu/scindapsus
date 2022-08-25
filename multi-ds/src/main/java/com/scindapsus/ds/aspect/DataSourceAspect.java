package com.scindapsus.ds.aspect;

import com.scindapsus.ds.RoutingDataSourceContext;
import com.scindapsus.ds.annotation.WithDataSource;
import com.scindapsus.ds.constants.DSConstants;
import com.scindapsus.ds.tx.TxUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author Java课代表
 * @author wyh
 * @since 1.0
 */
@Aspect
// 指定优先级高于@Transactional的默认优先级
// 从而保证先切换数据源再进行事务操作
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DataSourceAspect {

    @Around("@annotation(withDataSource)")
    public Object around(ProceedingJoinPoint pjp, WithDataSource withDataSource) throws Throwable {
        // 1.获取 @WithDataSource 注解中指定的数据源
        String routingKey = withDataSource.value();
        // 2.设置数据源上下文
        RoutingDataSourceContext.setRoutingKey(routingKey);
        // 3.本地事务
        boolean tx = withDataSource.tx();
        if (tx) {
            MDC.put(DSConstants.MDC_LOCAL_TX_ID, TxUtil.begin());
        }
        boolean state = true;
        // 4.使用设定好的数据源处理业务
        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            state = false;
            throw e;
        } finally {
            // 5.回滚或提交事务
            if (tx) {
                if (state) {
                    TxUtil.commit();
                } else {
                    TxUtil.rollback();
                }
            }
            // 6.清空数据源上下文
            RoutingDataSourceContext.reset();
        }
        return result;
    }
}