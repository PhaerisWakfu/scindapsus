package com.scindapsus.ds;

import com.scindapsus.ds.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wyh
 * @date 2022/8/22 16:27
 */
class DSTest extends BaseTest {

    @Autowired
    private CarService carService;

    @Test
    void testSwitch() {
        System.out.println(carService.firstGet());
        System.out.println(carService.secondGet());
    }
}
