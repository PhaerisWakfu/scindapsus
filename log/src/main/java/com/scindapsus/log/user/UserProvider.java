package com.scindapsus.log.user;

/**
 * @author wyh
 * @date 2021/10/9 14:25
 */
public interface UserProvider<T> {

    /**
     * 获取userId
     *
     * @return 用户ID
     */
    T getUserId();
}
