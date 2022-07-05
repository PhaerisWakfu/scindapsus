package com.scindapsus.graalvm;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;

/**
 * @author wyh
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraalVMUtil {

    private static final Logger log = LoggerFactory.getLogger(GraalVMUtil.class);

    /**
     * 执行指定脚本中的方法
     *
     * @param lang         语言(e.g: js)
     * @param function     脚本成员(e.g: 方法名)
     * @param responseType 返回值类型
     * @param args         调用成员的参数
     * @return 获取到的成员, 可能为空
     */
    public static <T> T execute(String lang, String function, Class<T> responseType, Object... args) {
        try (Context context = Context.create(lang)) {
            Value func = context.eval(lang, function);
            return func.canExecute() ? func.execute(args).as(responseType) : null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 执行原始语言中的方法
     *
     * @param lang         语言(e.g: js)
     * @param sourceFile   脚本文件
     * @param function     脚本方法(e.g: 方法名)
     * @param responseType 返回值类型
     * @param args         调用成员的参数
     * @return 获取到的成员, 可能为空
     */
    public static <T> T execute(String lang, File sourceFile, String function, Class<T> responseType, Object... args) {
        try (Context context = Context.create(lang)) {
            if (sourceFile != null) {
                Source source = Source.newBuilder(lang, sourceFile)
                        .build();
                context.eval(source);
            }
            Value func = context.getBindings(lang).getMember(function);
            return func.canExecute() ? func.execute(args).as(responseType) : null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
