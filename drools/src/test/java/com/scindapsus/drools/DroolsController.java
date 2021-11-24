package com.scindapsus.drools;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyh
 * @date 2021/11/15 16:36
 */
@RequestMapping("/drools")
@RestController
public class DroolsController {

    @GetMapping
    public Order test(Order order) {
        return DroolsHolder.fire(order);
    }
}
