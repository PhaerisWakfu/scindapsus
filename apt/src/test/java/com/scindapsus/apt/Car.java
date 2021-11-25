package com.scindapsus.apt;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * @author wyh
 * @date 2021/6/9 14:15
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
