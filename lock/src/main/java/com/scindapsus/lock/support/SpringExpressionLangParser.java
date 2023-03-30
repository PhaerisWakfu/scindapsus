package com.scindapsus.lock.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author wyh
 * @since 1.0
 */
public class SpringExpressionLangParser {

    private static final Logger log = LoggerFactory.getLogger(SpringExpressionLangParser.class);

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final ParameterNameDiscoverer NAME_DISCOVERER
            = new LocalVariableTableParameterNameDiscoverer();

    private SpringExpressionLangParser() {
    }

    /**
     * 解析表达式
     *
     * @param instance   method所在实例对象
     * @param expression 表达式
     * @param method     目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object instance, String expression, Method method, Object[] args) {
        try {
            log.info("expression -> {}", expression);
            Method instanceMethod = instance.getClass().getMethod(method.getName(), method.getParameterTypes());
            String[] paraNameArr = NAME_DISCOVERER.getParameterNames(instanceMethod);
            if (paraNameArr == null) {
                return expression;
            }
            StandardEvaluationContext context = new MethodBasedEvaluationContext(instance, instanceMethod, args, NAME_DISCOVERER);
            for (int i = 0; i < paraNameArr.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
            String value = PARSER.parseExpression(expression).getValue(context, String.class);
            log.info("parsed -> {}", value);
            return value;
        } catch (NoSuchMethodException e) {
            return expression;
        }
    }
}
