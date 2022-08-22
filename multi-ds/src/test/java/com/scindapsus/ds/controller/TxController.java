package com.scindapsus.ds.controller;

import com.scindapsus.ds.tx.TxCarService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wyh
 * @date 2022/8/22 17:27
 */
@RestController
@RequestMapping("/ds/tx")
public class TxController {

    private final TxCarService txCarService;

    public TxController(TxCarService txCarService) {
        this.txCarService = txCarService;
    }

    /**
     * 无声明式事务抛错
     */
    @GetMapping("/off")
    public String txOff() {
        txCarService.tx1OffAndThrow();
        return "success";
    }

    /**
     * 有声明式事务抛错
     */
    @GetMapping("/on")
    public String txOn() {
        txCarService.tx2OnAndThrow();
        return "success";
    }

    /**
     * 混合事务抛错
     */
    @GetMapping("/mix-off")
    public String mixOff() {
        txCarService.tx1On();
        txCarService.tx2OnAndThrow();
        return "success";
    }

    /*下面两个例子说明routingDs的事务是只支持单数据源的*/
    /*并且有嵌套事务时候，不抛错的话只会执行最后一个的事务*/

    /**
     * 混合事务
     */
    @GetMapping("/mix")
    @Transactional
    public String mix() {
        txCarService.tx2On();
        txCarService.tx1On();
        return "success";
    }

    /**
     * 混合事务嵌套抛错
     */
    @GetMapping("/mix-on")
    @Transactional
    public String mixOn() {
        txCarService.tx2OnAndThrow();
        txCarService.tx1On();
        return "success";
    }
}
