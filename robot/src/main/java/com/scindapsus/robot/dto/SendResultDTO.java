package com.scindapsus.robot.dto;


import lombok.Data;

/**
 * @author wyh
 * @since 2022/8/29
 */
@Data
public class SendResultDTO {

    /**
     * 出错返回码，为0表示成功，非0表示调用失败
     */
    private String errcode;

    /**
     * 返回码提示语
     */
    private String errmsg;
}
