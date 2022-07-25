package com.scindapsus.apt;


import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * @author wyh
 * @since 1.0
 */
@FastJPA
@Entity
public class Car {

    @Id
    private Long id;

    private String name;

    private String brand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
