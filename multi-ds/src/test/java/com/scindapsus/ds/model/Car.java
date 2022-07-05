package com.scindapsus.ds.model;

import lombok.Data;

import java.util.Date;

/**
 * @author wyh
 * @date 2022/7/5 10:27
 */
@Data
public class Car {

    private Long id;

    private String name;

    private String brand;

    private String age;

    private String area;

    private String color;

    private Date createTime;

    private Date updateTime;
}
