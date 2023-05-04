package com.scindapsus.tenant.interceptor;

import com.scindapsus.tenant.TenantHolder;
import com.scindapsus.tenant.TenantProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wyh
 * @since 2022/10/12
 */
public class TenantInterceptor implements HandlerInterceptor {

    private final TenantProvider tenantProvider;

    public TenantInterceptor(TenantProvider tenantProvider) {
        this.tenantProvider = tenantProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (TenantHolder.getTenant() != null) {
            return true;
        }
        //从请求中获取租户信息放到调用链中
        TenantHolder.setTenant(tenantProvider.getTenantValue(request));
        return true;
    }
}
