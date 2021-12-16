package com.scindapsus.apt;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * @author wyh
 * @since 1.0
 */
@FastJPA
@Data
@Entity
public class Car {

    @Id
    private Long id;

    private String name;

    private String brand;
}
