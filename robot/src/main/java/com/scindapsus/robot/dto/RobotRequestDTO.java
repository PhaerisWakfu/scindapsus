package com.scindapsus.robot.dto;

/**
 * @author wyh
 * @date 2022/8/26 15:34
 */
public class RobotRequestDTO {

    private String msgtype;

    private Markdown markdown;

    public RobotRequestDTO(String msgtype, Markdown markdown) {
        this.msgtype = msgtype;
        this.markdown = markdown;
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
}
