package com.scindapsus.robot;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 暂时只支持text与markdown格式的消息
 *
 * @author wyh
 * @date 2022/8/26 14:38
 */
public abstract class AbstractRobotService {

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
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public WorkWechatResponse sendMdMsg(String robotUrl, String template, @Nullable String sql) {
        return sendTmpMsg(false, true, robotUrl, template, sql);
    }

    /**
     * 发送markdown模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public WorkWechatResponse sendTxtMsg(String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        return sendTmpMsg(false, false, robotUrl, template, sql, atMobiles);
    }

    /**
     * 发送多行结果集markdown模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public WorkWechatResponse sendMultiRstMdMsg(String robotUrl, String template, @Nullable String sql) {
        return sendTmpMsg(true, true, robotUrl, template, sql);
    }

    /**
     * 发送多行结果集markdown模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public WorkWechatResponse sendMultiRstTxtMsg(String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        return sendTmpMsg(true, false, robotUrl, template, sql, atMobiles);
    }

    /**
     * 发送模板消息（支持ST模板与SQL赋值）
     *
     * @param multiResult 是否是多行结果集
     * @param markdown    是否是markdown格式的内容
     * @param robotUrl    机器人hook地址
     * @param template    string template模板或普通文本
     * @param sql         {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @param atMobiles   at人的手机号列表
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    private WorkWechatResponse sendTmpMsg(boolean multiResult, boolean markdown, String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        //发送内容
        String content = template;
        //发送标识
        boolean sendFlag = false;
        if (StringUtils.hasText(sql)) {
            JdbcTemplate jdbcTemplate = setJdbcTemplate();
            ST st = new ST(template, StringTemplateConstants.DELIMITER, StringTemplateConstants.DELIMITER);
            if (multiResult) {
                List<Map<String, Object>> params = jdbcTemplate.queryForList(sql);
                if (CollectionUtil.isNotEmpty(params)) {
                    sendFlag = true;
                    //填充模板
                    st.add(StringTemplateConstants.MULTI_RESULT_PARAM_NAME, params);
                }
            } else {
                Map<String, Object> param = jdbcTemplate.queryForMap(sql);
                if (!param.isEmpty()) {
                    sendFlag = true;
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
                }
            }
            //模板解析后的内容
            content = sendFlag ? st.render() : null;
        }
        //发送
        return Optional.ofNullable(content)
                .map(x -> markdown ? markdownMsg(robotUrl, x) : textMsg(robotUrl, x, atMobiles))
                //不发送消息
                .orElse(null);
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
