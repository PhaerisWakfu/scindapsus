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
        System.out.println(myRobotServiceImpl.sendTxtMsg("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=27dd9bf2-bc90-4b4a-a897-89c2a9fb3e62",
                "hello $brand$", "select brand from car where id=1", "@all"));
    }
}
