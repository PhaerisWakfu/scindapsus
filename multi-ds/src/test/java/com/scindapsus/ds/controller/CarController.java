package com.scindapsus.ds.controller;

import com.scindapsus.ds.model.Car;
import com.scindapsus.ds.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyh
 * @date 2022/7/5 11:11
 */
@RestController
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/ds/1")
    public Car first() {
        return carService.firstGet();
    }

    @GetMapping("/ds/2")
    public Car second() {
        return carService.secondGet();
    }


}
