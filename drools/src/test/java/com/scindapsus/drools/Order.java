package com.scindapsus.drools;


/**
 * @author wyh
 * @since 2022/7/25
 */
public class Order {

    private Double originalPrice;

    private Double realPrice;

    public Order() {
    }

    public Order(Double originalPrice, Double realPrice) {
        this.originalPrice = originalPrice;
        this.realPrice = realPrice;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }
}
