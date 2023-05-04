package com.scindapsus.robot.dto;

import java.util.List;

/**
 * @author wyh
 * @since 2022/8/29
 */
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

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Markdown getMarkdown() {
        return markdown;
    }

    public void setMarkdown(Markdown markdown) {
        this.markdown = markdown;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }


    public static class Markdown {

        private String content;

        public Markdown(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    public static class Text {

        private String content;

        private List<String> mentioned_mobile_list;

        public Text(String content) {
            this.content = content;
        }

        public Text(String content, List<String> mentionedMobileList) {
            this.content = content;
            this.mentioned_mobile_list = mentionedMobileList;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getMentioned_mobile_list() {
            return mentioned_mobile_list;
        }

        public void setMentioned_mobile_list(List<String> mentioned_mobile_list) {
            this.mentioned_mobile_list = mentioned_mobile_list;
        }
    }
}
