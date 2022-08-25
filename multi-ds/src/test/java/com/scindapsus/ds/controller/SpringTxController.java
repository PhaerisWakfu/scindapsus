package com.scindapsus.ds.controller;

import com.scindapsus.ds.tx.SpringTxCarService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wyh
 * @date 2022/8/22 17:27
 */
//@EnableTransactionManagement
//@RestController
@RequestMapping("/ds/tx")
public class SpringTxController {

    private final SpringTxCarService springTxCarService;

    public SpringTxController(SpringTxCarService springTxCarService) {
        this.springTxCarService = springTxCarService;
    }


    /**
     * 单数据源无事务抛错
     */
    @GetMapping("/off/throw")
    public String txOffThrow() {
        springTxCarService.tx1OffAndThrow();
        return "success";
    }

    /**
     * 单数据源有事务抛错
     */
    @GetMapping("/on/throw")
    public String txOnThrow() {
        springTxCarService.tx2OnAndThrow();
        return "success";
    }

    /**
     * 单数据源有事务不抛错
     */
    @GetMapping("/on")
    public String txOn() {
        springTxCarService.tx2On();
        return "success";
    }

    /**
     * 多数据源无事务抛错
     */
    @GetMapping("/mix/off/throw")
    public String mixOffThrow() {
        springTxCarService.tx1On();
        springTxCarService.tx2OnAndThrow();
        return "success";
    }

    /**
     * 多数据源无事务不抛错
     */
    @GetMapping("/mix/off")
    public String mixOff() {
        springTxCarService.tx1On();
        springTxCarService.tx2On();
        return "success";
    }

    /*下面两个例子说明routingDs的事务是只支持单数据源的*/
    /*有嵌套事务时候，会认为是走默认数据源*/
    /*例如：更新ds1的id为1和2的数据与ds2的id为3和4的数据，会变成更新ds1的id为1、2、3、4的数据*/

    /**
     * 多数据源有事务不抛错
     */
    @GetMapping("/mix/on")
    @Transactional
    public String mixOn() {
        springTxCarService.tx1On();
        springTxCarService.tx2On();
        return "success";
    }

    /**
     * 多数据源有事务抛错
     */
    @GetMapping("/mix/on/throw")
    @Transactional
    public String mixOnThrow() {
        springTxCarService.tx1On();
        springTxCarService.tx2OnAndThrow();
        return "success";
    }
}
