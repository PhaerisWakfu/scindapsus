package com.scindapsus.robot.dto;


/**
 * @author wyh
 * @since 1.0
 */
public class SendResultDTO {

    /**
     * 出错返回码，为0表示成功，非0表示调用失败
     */
    private String errcode;

    /**
     * 返回码提示语
     */
    private String errmsg;


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "SendResultDTO{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
