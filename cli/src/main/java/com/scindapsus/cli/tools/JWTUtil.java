package com.scindapsus.cli.tools;

import cn.hutool.jwt.JWT;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author wyh
 * @since 2022/6/8 14:36
 */
public class JWTUtil {

    /**
     * 校验jwt token
     *
     * @param token  token串
     * @param secret 秘钥
     * @return 是否校验通过
     */
    public static boolean verify(String token, String secret) {
        //解析token
        JWT jwt = explain(token);
        return verify(jwt, secret);
    }

    /**
     * 校验jwt token
     *
     * @param jwt    字符串token转换的jwt对象
     * @param secret 秘钥
     * @return 是否校验通过
     */
    public static boolean verify(JWT jwt, String secret) {
        //设置私钥
        jwt.setKey(secret.getBytes(StandardCharsets.UTF_8));
        return jwt.validate(0);
    }

    /**
     * 简单生成jwt token(1小时过期)
     * 生成复杂的带参数的token详见{@link JWT#create()}
     *
     * @param secret 秘钥
     * @return jwt token
     */
    public static String create(String secret) {
        return create(secret, null);
    }

    /**
     * 简单生成jwt token(1小时过期)
     * 生成复杂的带参数的token详见{@link JWT#create()}
     *
     * @param secret   秘钥
     * @param payloads 载荷
     * @return jwt token
     */
    public static String create(String secret, Map<String, ?> payloads) {
        long issuedAt = System.currentTimeMillis();
        long expiresAt = Duration.ofHours(1).toMillis() + issuedAt;
        JWT jwt = JWT.create()
                .setKey(secret.getBytes(StandardCharsets.UTF_8))
                .setIssuedAt(new Date(issuedAt))
                .setExpiresAt(new Date(expiresAt));
        Optional.ofNullable(payloads).ifPresent(jwt::addPayloads);
        return jwt.sign();
    }

    /**
     * 解析jwt字符串token
     *
     * @param token 传入的token
     * @return 解析后的jwt对象
     */
    public static JWT explain(String token) {
        //解析token
        return JWT.of(token);
    }

    /**
     * 获取payload
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPayload(String token, String name) {
        JWT explain = explain(token);
        return (T) explain.getPayload(name);
    }
}
