package com.scindapsus.robot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author wyh
 * @since 2022/8/29
 */
@Data
public class CorpWechatRobotRequestDTO {

    private String msgtype;

    private Markdown markdown;

    private Text text;

    public CorpWechatRobotRequestDTO(String msgtype, Markdown markdown) {
        this.msgtype = msgtype;
        this.markdown = markdown;
    }

    public CorpWechatRobotRequestDTO(String msgtype, Text text) {
        this.msgtype = msgtype;
        this.text = text;
    }


    @Data
    @AllArgsConstructor
    public static class Markdown {

        private String content;
    }


    @Data
    @AllArgsConstructor
    public static class Text {

        private String content;

        private List<String> mentioned_mobile_list;

        public Text(String content) {
            this.content = content;
        }
    }
}
