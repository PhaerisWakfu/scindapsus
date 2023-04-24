package com.scindapsus.calcite.config;

import com.scindapsus.calcite.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;

/**
 * @author wyh
 * @since 2022/10/14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CalciteProperties.PREFIX)
public class CalciteProperties {

    public static final String PREFIX = "scindapsus.calcite";

    private LinkedList<Schema> schemas;
}
