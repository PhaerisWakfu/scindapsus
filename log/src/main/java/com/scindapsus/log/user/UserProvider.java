package com.scindapsus.log.user;

/**
 * @author wyh
 * @since 1.0
 */
public interface UserProvider<T> {

    /**
     * 获取userId
     *
     * @return 用户ID
     */
    T getUserId();
}
