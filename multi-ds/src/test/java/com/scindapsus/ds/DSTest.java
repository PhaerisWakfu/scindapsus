package com.scindapsus.ds;

import com.scindapsus.ds.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wyh
 * @since 2022/7/4
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
