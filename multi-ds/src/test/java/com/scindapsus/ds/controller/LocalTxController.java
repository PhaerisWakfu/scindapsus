package com.scindapsus.ds.controller;

import com.scindapsus.ds.tx.LocalTxCarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyh
 * @date 2022/8/25 14:44
 */
@RestController
@RequestMapping("/ds/tx/local")
public class LocalTxController {

    private final LocalTxCarService localTxCarService;

    public LocalTxController(LocalTxCarService localTxCarService) {
        this.localTxCarService = localTxCarService;
    }

    @GetMapping("/throw")
    public String ds2Throw() {
        localTxCarService.ds2Throw();
        return "success";
    }

    @GetMapping("/nested")
    public String nested() {
        localTxCarService.nested();
        return "success";
    }

    @GetMapping("/nested/throw")
    public String nestedThrow() {
        localTxCarService.nestedThrow();
        return "success";
    }

}
