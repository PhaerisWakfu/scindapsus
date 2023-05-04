package com.scindapsus.tenant;

import com.scindapsus.tenant.config.TenantProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wyh
 * @since 2022/10/12
 */
public class PropertiesHolder {

    private static TenantProperties tenantProperties;

    @Autowired
    @SuppressWarnings("all")
    public void setUp(TenantProperties tenantProperties) {
        PropertiesHolder.tenantProperties = tenantProperties;
    }

    public static String tenantName() {
        return tenantProperties.getName();
    }

    public static boolean mdc() {
        return tenantProperties.isMdc();
    }

    public static TenantProperties.Propagation propagation() {
        return tenantProperties.getPropagation();
    }
}
