package com.scindapsus.lock.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SPEL解析器
 *
 * @author wyh
 * @date  2021/10/9 10:49
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringExpressionLangParser {

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();


    /**
     * @param rootObject method所在的对象
     * @param expression 表达式
     * @param method     目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject, String expression, Method method, Object[] args) {
        String[] paraNameArr = NAME_DISCOVERER.getParameterNames(method);
        if (paraNameArr == null) {
            return null;
        }
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, NAME_DISCOVERER);
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return PARSER.parseExpression(expression).getValue(context, String.class);
    }
}
