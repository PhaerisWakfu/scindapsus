package com.scindapsus.drools;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author wyh
 * @since 1.0
 */
public class DroolsHolder {

    private static KieContainer kieContainer;

    @Autowired
    @SuppressWarnings("all")
    public void setUp(KieContainer kieContainer) {
        DroolsHolder.kieContainer = kieContainer;
    }

    /**
     * 执行规则
     *
     * @param entity   需要被执行规则的对象
     * @param <T>      对象类型
     * @return 执行规则后的对象
     */
    public static <T> T fire(T entity) {
        return fire(entity, kieSession -> kieSession.insert(entity));
    }

    /**
     * 执行规则
     *
     * @param entity   需要被执行规则的对象
     * @param <T>      对象类型
     * @return 执行规则后的对象
     */
    public static <T> List<T> fire(List<T> entity) {
        return fire(entity, kieSession -> entity.forEach(kieSession::insert));
    }

    /**
     * 执行规则
     *
     * @param entity   需要被执行规则的对象
     * @param consumer 执行动作
     * @param <T>      对象类型
     * @return 执行规则后的对象
     */
    public static <T> T fire(T entity, Consumer<KieSession> consumer) {
        KieSession kieSession = kieContainer.newKieSession();
        consumer.accept(kieSession);
        kieSession.fireAllRules();
        kieSession.dispose();
        return entity;
    }
}
