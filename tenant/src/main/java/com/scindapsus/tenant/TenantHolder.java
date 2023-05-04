package com.scindapsus.tenant;

import brave.baggage.BaggageField;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * @author wyh
 * @since 2022/10/12
 */
public class TenantHolder {

    /**
     * 获取租户
     */
    @Nullable
    public static String getTenant() {
        return Optional.ofNullable(BaggageField.getByName(PropertiesHolder.tenantName()))
                .map(BaggageField::getValue)
                .orElse(null);
    }

    /**
     * 设置租户
     *
     * @param tenant 租户值
     */
    public static void setTenant(String tenant) {
        Optional.ofNullable(BaggageField.getByName(PropertiesHolder.tenantName()))
                .ifPresent(x -> x.updateValue(tenant));
    }
}
