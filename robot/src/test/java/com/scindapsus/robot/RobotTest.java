package com.scindapsus.robot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wyh
 * @date 2022/8/29 15:08
 */
public class RobotTest extends BaseTest {

    @Autowired
    private MyRobotServiceImpl myRobotServiceImpl;

    @Test
    public void send() {
        System.out.println(myRobotServiceImpl.sendMsg("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=60e707a9-609d-4e24-9a95-de39660023e5",
                "hello $brand$", "select brand from car where id=1"));
    }
}
