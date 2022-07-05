package com.scindapsus.ds.service;

import com.scindapsus.ds.annotation.WithDataSource;
import com.scindapsus.ds.mapper.CarMapper;
import com.scindapsus.ds.model.Car;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author wyh
 * @date 2022/7/5 15:34
 */
@Service
@AllArgsConstructor
public class CarService {

    private final CarMapper carMapper;

    public Car firstGet() {
        return carMapper.getOne(1L);
    }

    @WithDataSource("second")
    public Car secondGet() {
        return carMapper.getOne(1L);
    }
}
