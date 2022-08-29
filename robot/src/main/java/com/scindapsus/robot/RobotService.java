package com.scindapsus.robot;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.scindapsus.robot.constants.RobotConstant;
import com.scindapsus.robot.constants.StringTemplateConstants;
import com.scindapsus.robot.dto.RobotRequestDTO;
import com.scindapsus.robot.dto.WorkWechatResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.stringtemplate.v4.ST;

import java.util.Arrays;
import java.util.Map;

/**
 * 暂时只支持text与markdown格式的消息
 *
 * @author wyh
 * @date 2022/8/26 14:38
 */
public abstract class RobotService {

    /**
     * 设置jdbcTemplate
     */
    public abstract JdbcTemplate setJdbcTemplate();

    /**
     * 设置restTemplate
     */
    public abstract RestTemplate setRestTemplate();

    /**
     * 发送markdown模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return 发送结果
     */
    public WorkWechatResponse sendMdMsg(String robotUrl, String template, @Nullable String sql) {
        return sendTmpMsg(true, robotUrl, template, sql);
    }

    /**
     * 发送markdown模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return 发送结果
     */
    public WorkWechatResponse sendTxtMsg(String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        return sendTmpMsg(false, robotUrl, template, sql, atMobiles);
    }

    /**
     * 发送模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return 发送结果
     */
    private WorkWechatResponse sendTmpMsg(boolean markdown, String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        //发送内容
        String content = template;
        if (StringUtils.hasText(sql)) {
            JdbcTemplate jdbcTemplate = setJdbcTemplate();
            Map<String, Object> param = jdbcTemplate.queryForMap(sql);
            ST st = new ST(template, StringTemplateConstants.DELIMITER, StringTemplateConstants.DELIMITER);
            //填充模板
            BeanUtil.beanToMap(param, false, true)
                    .forEach((key, value) -> {
                        if (value instanceof String) {
                            if (StringUtils.hasText((String) value)) {
                                st.add(key, value);
                            }
                        } else {
                            st.add(key, value);
                        }
                    });
            //模板解析后的内容
            content = st.render();
        }
        //发送
        return markdown ? markdownMsg(robotUrl, content) : textMsg(robotUrl, content, atMobiles);
    }

    /**
     * 发送普通markdown消息
     *
     * @param robotUrl        机器人hook地址
     * @param markdownContent markdown内容
     * @return 发送结果
     */
    private WorkWechatResponse markdownMsg(String robotUrl, String markdownContent) {
        RobotRequestDTO.Markdown markdown = new RobotRequestDTO.Markdown(markdownContent);
        RobotRequestDTO request = new RobotRequestDTO(RobotConstant.MARKDOWN_MESSAGE_TYPE, markdown);
        return setRestTemplate().postForObject(robotUrl, request, WorkWechatResponse.class);
    }

    /**
     * 发送普通text消息
     *
     * @param robotUrl    机器人hook地址
     * @param textContent text内容
     * @param atMobiles   @群聊的某些人，手机号代表人，@all代表所有人
     * @return 发送结果
     */
    private WorkWechatResponse textMsg(String robotUrl, String textContent, String... atMobiles) {
        RobotRequestDTO.Text text = new RobotRequestDTO.Text(textContent);
        if (ArrayUtil.isNotEmpty(atMobiles)) {
            text = new RobotRequestDTO.Text(textContent, Arrays.asList(atMobiles));
        }
        RobotRequestDTO request = new RobotRequestDTO(RobotConstant.TEXT_MESSAGE_TYPE, text);
        return setRestTemplate().postForObject(robotUrl, request, WorkWechatResponse.class);
    }
}
