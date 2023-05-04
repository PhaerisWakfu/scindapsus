package com.scindapsus.tenant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wyh
 * @since 2022/10/12
 */
public interface TenantProvider {

    /**
     * 从请求中的各种参数获取tenant
     *
     * @param request 请求
     * @return tenant value
     */
    String getTenantValue(HttpServletRequest request);
}
