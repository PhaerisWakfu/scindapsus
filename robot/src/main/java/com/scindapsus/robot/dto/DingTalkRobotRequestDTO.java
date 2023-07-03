package com.scindapsus.robot.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author wyh
 * @since 2022/8/29
 */
@Data
public class DingTalkRobotRequestDTO {

    private String msgtype;

    private Markdown markdown;

    private At at;

    @Data
    @AllArgsConstructor
    public static class Markdown {

        private String title;

        private String text;
    }

    @Data
    @AllArgsConstructor
    public static class At {

        private boolean isAtAll;

        private List<String> atMobiles;
    }
}
