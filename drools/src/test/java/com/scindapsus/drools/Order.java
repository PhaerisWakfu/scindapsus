package com.scindapsus.drools;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wyh
 * @date 2021/11/15 15:39
 */
@Data
@AllArgsConstructor
public class Order {

    private Double originalPrice;

    private Double realPrice;
}
