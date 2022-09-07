package com.scindapsus.robot;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.scindapsus.robot.constants.RobotConstant;
import com.scindapsus.robot.constants.StringTemplateConstants;
import com.scindapsus.robot.dto.CorpWechatRobotRequestDTO;
import com.scindapsus.robot.dto.DingTalkRobotRequestDTO;
import com.scindapsus.robot.dto.SendResultDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.stringtemplate.v4.ST;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
     * 发送钉钉消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param secret   秘钥
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO dingTalkMsg(String robotUrl, String title, String template, @Nullable String secret, @Nullable String sql, String... atMobiles) {
        return sendDingTalkTmpMsg(robotUrl, title, template, secret, sql, false, atMobiles);
    }

    /**
     * 发送钉钉SQL多行结果集消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param secret   秘钥
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO dingTalkMultiRetMsg(String robotUrl, String title, String template, @Nullable String secret, @Nullable String sql, String... atMobiles) {
        return sendDingTalkTmpMsg(robotUrl, title, template, secret, sql, true, atMobiles);
    }

    /**
     * 发送钉钉模板消息（支持ST模板与SQL赋值）
     *
     * @param robotUrl    机器人hook地址
     * @param template    string template模板或普通文本
     * @param secret      秘钥
     * @param sql         {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @param multiResult 是否是多行结果集
     * @param atMobiles   at人的手机号列表(只有text类型消息才支持at)
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    private SendResultDTO sendDingTalkTmpMsg(String robotUrl, String title, String template,
                                             String secret, @Nullable String sql, boolean multiResult, String... atMobiles) {
        //获取发送内容
        String content = renderMessage(template, sql, multiResult);
        //发送
        return Optional.ofNullable(content)
                .map(x -> sendDingTalkMsg(robotUrl, title, x, secret, atMobiles))
                //不发送消息
                .orElse(null);
    }

    /**
     * 发送钉钉普通markdown消息
     *
     * @param robotUrl  机器人hook地址
     * @param title     内容标题
     * @param content   markdown内容
     * @param secret    秘钥
     * @param atMobiles at人列表
     * @return 发送结果
     */
    private SendResultDTO sendDingTalkMsg(String robotUrl, String title, String content, String secret, String... atMobiles) {
        DingTalkRobotRequestDTO request = new DingTalkRobotRequestDTO();
        request.setMsgtype(RobotConstant.MARKDOWN_MESSAGE_TYPE);
        if (ArrayUtil.isNotEmpty(atMobiles)) {
            boolean isAtAll = ArrayUtil.contains(atMobiles, RobotConstant.ROBOT_AT_ALL);
            List<String> finalAt = CollectionUtil.newArrayList(atMobiles);
            finalAt.remove(RobotConstant.ROBOT_AT_ALL);
            request.setAt(new DingTalkRobotRequestDTO.At(isAtAll, finalAt));
            //钉钉at人不仅要传at参数，而且要在内容中添加文本@xxx
            if (!isAtAll) {
                StringBuilder contentBuilder = new StringBuilder(content);
                finalAt.forEach(at -> contentBuilder.append("@").append(at));
                content = contentBuilder.toString();
            }
        }
        request.setMarkdown(new DingTalkRobotRequestDTO.Markdown(title, content));
        if (StrUtil.isNotBlank(secret)) {
            robotUrl = dingTalkUrlSigned(robotUrl, secret);
        }
        return setRestTemplate().postForObject(robotUrl, request, SendResultDTO.class);
    }

    private String dingTalkUrlSigned(String url, String secret) {
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance(RobotConstant.SIGN_ALG);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), RobotConstant.SIGN_ALG));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8.name());
            return url + RobotConstant.TIMESTAMP_ARG + timestamp + RobotConstant.SIGN_ARG + sign;
        } catch (Exception e) {
            throw new IllegalArgumentException("加签失败", e);
        }
    }

    /**
     * 发送企微markdown消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO wechatMDMsg(String robotUrl, String template, @Nullable String sql) {
        return sendWechatTmpMsg(true, robotUrl, template, sql, false);
    }

    /**
     * 发送企微txt消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO wechatTxtMsg(String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        return sendWechatTmpMsg(false, robotUrl, template, sql, false, atMobiles);
    }

    /**
     * 发送企微SQL多行结果集markdown消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO wechatMultiRstMDMsg(String robotUrl, String template, @Nullable String sql) {
        return sendWechatTmpMsg(true, robotUrl, template, sql, true);
    }

    /**
     * 发送企微SQL多行结果集txt消息
     *
     * @param robotUrl 机器人hook地址
     * @param template string template模板或普通文本
     * @param sql      {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    public SendResultDTO wechatMultiRstTxtMsg(String robotUrl, String template, @Nullable String sql, String... atMobiles) {
        return sendWechatTmpMsg(false, robotUrl, template, sql, true, atMobiles);
    }

    /**
     * 发送企微模板消息（支持ST模板与SQL赋值）
     *
     * @param markdown    是否是markdown格式的内容
     * @param robotUrl    机器人hook地址
     * @param template    string template模板或普通文本
     * @param sql         {@code nullable}模板参数sql,如果template只是普通文本可以不传
     * @param multiResult 是否是多行结果集
     * @param atMobiles   at人的手机号列表(只有text类型消息才支持at)
     * @return {@code nullable}发送结果,为空则不发送消息
     */
    @Nullable
    private SendResultDTO sendWechatTmpMsg(boolean markdown, String robotUrl, String template,
                                           @Nullable String sql, boolean multiResult, String... atMobiles) {
        //获取发送内容
        String content = renderMessage(template, sql, multiResult);
        //发送
        return Optional.ofNullable(content)
                .map(x -> markdown ? sendWechatMarkdownMsg(robotUrl, x) : sendWechatTxtMsg(robotUrl, x, atMobiles))
                //不发送消息
                .orElse(null);
    }

    /**
     * 发送企微普通markdown消息
     *
     * @param robotUrl        机器人hook地址
     * @param markdownContent markdown内容
     * @return 发送结果
     */
    private SendResultDTO sendWechatMarkdownMsg(String robotUrl, String markdownContent) {
        CorpWechatRobotRequestDTO.Markdown markdown = new CorpWechatRobotRequestDTO.Markdown(markdownContent);
        CorpWechatRobotRequestDTO request = new CorpWechatRobotRequestDTO(RobotConstant.MARKDOWN_MESSAGE_TYPE, markdown);
        return setRestTemplate().postForObject(robotUrl, request, SendResultDTO.class);
    }

    /**
     * 发送企微普通text消息
     *
     * @param robotUrl    机器人hook地址
     * @param textContent text内容
     * @param atMobiles   @群聊的某些人，手机号代表人，@all代表所有人
     * @return 发送结果
     */
    private SendResultDTO sendWechatTxtMsg(String robotUrl, String textContent, String... atMobiles) {
        CorpWechatRobotRequestDTO.Text text = new CorpWechatRobotRequestDTO.Text(textContent);
        if (ArrayUtil.isNotEmpty(atMobiles)) {
            text = new CorpWechatRobotRequestDTO.Text(textContent, Arrays.asList(atMobiles));
        }
        CorpWechatRobotRequestDTO request = new CorpWechatRobotRequestDTO(RobotConstant.TEXT_MESSAGE_TYPE, text);
        return setRestTemplate().postForObject(robotUrl, request, SendResultDTO.class);
    }

    /**
     * 加工获取消息内容
     *
     * @param template       原始消息（支持stringTemplate）
     * @param sql            获取模板参数的sql
     * @param sqlMultiResult sql结果是否是多行的
     * @return 加工后的消息内容
     */
    @Nullable
    private String renderMessage(String template, @Nullable String sql, boolean sqlMultiResult) {
        //发送内容
        String content = template;
        //发送标识
        boolean sendFlag = false;
        if (StringUtils.hasText(sql)) {
            JdbcTemplate jdbcTemplate = setJdbcTemplate();
            ST st = new ST(template, StringTemplateConstants.DELIMITER, StringTemplateConstants.DELIMITER);
            if (sqlMultiResult) {
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
            //如果SQL内容为空则设置content为空(不发送消息)
            content = sendFlag ? st.render() : null;
        }
        return content;
    }
}
