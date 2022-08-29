package com.scindapsus.robot;

import cn.hutool.core.bean.BeanUtil;
import com.scindapsus.robot.constants.RobotConstant;
import com.scindapsus.robot.constants.StringTemplateConstants;
import com.scindapsus.robot.dto.RobotRequestDTO;
import com.scindapsus.robot.dto.WorkWechatResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.stringtemplate.v4.ST;

import java.util.Map;

/**
 * 暂时只支持markdown格式的消息
 *
 * @author wyh
 * @date 2022/8/26 14:38
 */
public interface RobotService {

    /**
     * 设置jdbcTemplate
     */
    JdbcTemplate setJdbcTemplate();

    /**
     * 设置restTemplate
     */
    RestTemplate setRestTemplate();


    /**
     * 发送消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板
     * @param sql      模板参数sql
     * @return 发送结果
     */
    default WorkWechatResponse sendMsg(String robotUrl, String template, String sql) {
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
        return sendMsg(robotUrl, content);
    }

    /**
     * 发送markdown消息
     *
     * @param robotUrl 机器人hook地址
     * @param content  发送内容
     * @return 发送结果
     */
    default WorkWechatResponse sendMsg(String robotUrl, String content) {
        RobotRequestDTO request = new RobotRequestDTO(RobotConstant.MARKDOWN_MESSAGE_TYPE, new RobotRequestDTO.Markdown(content));
        return setRestTemplate().postForObject(robotUrl, request, WorkWechatResponse.class);
    }
}
