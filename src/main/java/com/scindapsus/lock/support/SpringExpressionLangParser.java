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
 * @author wyh
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringExpressionLangParser {


    /**
     * @param rootObject method所在的对象
     * @param expression 表达式
     * @param method     目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject, String expression, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = nameDiscoverer.getParameterNames(method);
        if (paraNameArr == null) {
            return null;
        }
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, nameDiscoverer);
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(expression).getValue(context, String.class);
    }
}
