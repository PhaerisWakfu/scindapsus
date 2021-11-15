package com.scindapsus.drools;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * 初始化所有静态操作bean的工具
 *
 * @author wyh
 * @date 2021/1/7 17:56
 */
@AllArgsConstructor
public class AutowireStaticSmartInitialization implements SmartInitializingSingleton {

    private final AutowireCapableBeanFactory beanFactory;

    /**
     * 当所有的单例Bean初始化完成后，对static静态成员进行赋值
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 因为是给static静态属性赋值，因此这里new一个实例做注入是可行的
        beanFactory.autowireBean(new DroolsHolder());
    }
}