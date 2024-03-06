package com.scindapsus.cli;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.crypto.SecureUtil;
import com.scindapsus.cli.tools.JWTUtil;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * @author wyh
 * @since 2023/1/6
 */
@ShellComponent
public class CommandProvider {

    @ShellMethod(value = "create a jwt token", group = "jwt", key = "jwtc")
    public void jwtCreate(String secret) {
        System.out.println(JWTUtil.create(secret));
    }

    @ShellMethod(value = "verify jwt token", group = "jwt", key = "jwtv")
    public void jwtVerify(String token, String secret) {
        System.out.println(JWTUtil.verify(token, secret));
    }

    @ShellMethod(group = "secret", key = "md5")
    public void md5(String str) {
        System.out.println(SecureUtil.md5(str));
    }

    @ShellMethod(group = "secret", key = "64e")
    public void base64Encode(String str) {
        System.out.println(Base64.encode(str));
    }

    @ShellMethod(group = "secret", key = "64d")
    public void base64Decode(String str) {
        System.out.println(new String(Base64.decode(str)));
    }

    @ShellMethod(group = "secret", key = "64ue")
    public void base64UrlEncode(String str) {
        String s = Base64.encode(str);
        s = s.replaceAll("\\+", "-");
        s = s.replaceAll("/", "_");
        s = s.replaceAll("=", "");
        System.out.println(s);
    }

    @ShellMethod(group = "secret", key = "64ud")
    public void base64UrlDecode(String str) {
        String s = str;
        s = s.replaceAll("-", "+");
        s = s.replaceAll("_", "/");
        switch (s.length() % 4) {
            case 2:
                s += "==";
                break;
            case 3:
                s += "=";
                break;
            default:
                break;
        }
        System.out.println(new String(Base64.decode(s)));
    }

    @ShellMethod(group = "cmd", key = "ip")
    public void ip() {
        System.out.println(RuntimeUtil.execForStr("ipconfig"));
    }
}
