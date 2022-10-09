package com.scindapsus.robot;

import com.scindapsus.robot.dto.SendResultDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @author wyh
 * @date 2022/8/29 15:08
 */
public class RobotTest extends BaseTest {

    @Autowired
    private RobotMsgSender robotMsgSender;

    /**
     * 固定简单消息
     */
    @Test
    public void wechatNormalMsg() {
        SendResultDTO result = robotMsgSender.sendWxTxtMsg(
                "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=60e707a9-609d-4e24-9a95-de39660023e5",
                "hello world",
                null);
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 带SQL变量的复杂消息,需要有jdbcTemplate
     */
    @Test
    public void wechat() {
        SendResultDTO result = robotMsgSender.sendWxTxtMsg(
                "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=xxx",
                "hello $brand$",
                "select brand from car where id=1",
                "@all");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 单行SQL变量的消息
     */
    @Test
    public void dingTalk() {
        SendResultDTO result = robotMsgSender.sendDingMsg(
                "https://oapi.dingtalk.com/robot/send?access_token=xxx",
                "scindapsus",
                "hello $brand$",
                "xxx",
                "select brand from car where id=1",
                "@all");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }

    /**
     * 多行SQL变量的消息
     */
    @Test
    public void multiSQLResult() {
        SendResultDTO result = robotMsgSender.sendDingMultiRstMsg(
                "https://oapi.dingtalk.com/robot/send?access_token=xxx",
                "scindapsus",
                "#### 总数==>$first(params).ct$\n" +
                        "$params:{p|\n" +
                        "> 名称：$p.name$，品牌:$p.brand$\n" +
                        "};separator=\"\\n\"$",
                "xxx",
                "SELECT t1.*, t2.* FROM( SELECT `name`, `brand` FROM `car` u) t1, (SELECT count(1) ct FROM `car`) t2",
                "18321809917");
        Optional.ofNullable(result).ifPresent(x -> Assertions.assertEquals("0", x.getErrcode()));
    }
}
