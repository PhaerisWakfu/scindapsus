package com.scindapsus.robot.dto;


import java.util.List;

/**
 * @author wyh
 * @date 2022/8/26 15:34
 */
public class DingTalkRobotRequestDTO {

    private String msgtype;

    private Markdown markdown;

    private At at;

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

    public At getAt() {
        return at;
    }

    public void setAt(At at) {
        this.at = at;
    }

    public static class Markdown {

        private String title;

        private String text;

        public Markdown(String title, String text) {
            this.title = title;
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class At {

        private boolean isAtAll;

        private List<String> atMobiles;

        public At(boolean isAtAll, List<String> atMobiles) {
            this.isAtAll = isAtAll;
            this.atMobiles = atMobiles;
        }

        public boolean getIsAtAll() {
            return isAtAll;
        }

        public void setIsAtAll(boolean atAll) {
            isAtAll = atAll;
        }

        public List<String> getAtMobiles() {
            return atMobiles;
        }

        public void setAtMobiles(List<String> atMobiles) {
            this.atMobiles = atMobiles;
        }
    }
}
