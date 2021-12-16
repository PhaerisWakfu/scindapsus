package com.scindapsus.drools;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wyh
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Order {

    private Double originalPrice;

    private Double realPrice;
}
