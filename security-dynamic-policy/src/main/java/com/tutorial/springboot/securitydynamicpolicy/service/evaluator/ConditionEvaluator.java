package com.tutorial.springboot.securitydynamicpolicy.service.evaluator;

import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

@Service
public class ConditionEvaluator {

    private final Logger logger = LoggerFactory.getLogger(CriteriaEvaluator.class);

    private final ExpressionParser parser = new SpelExpressionParser();

    public boolean evaluateCondition(String condition, Object criteriaResult, Policy policy, HttpServletRequest request) {
        if (condition == null || condition.isEmpty()) {
            return false;
        }

        try {
            var expression = parser.parseExpression(condition);
            var context = new StandardEvaluationContext();
            context.setVariable("criteria", criteriaResult);
            context.setVariable("request", request);
            context.setVariable("policy", policy);

            return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
        } catch (Exception e) {
            logger.error("Condition script evaluation failed: {}", e.getMessage());
            return false;
        }
    }
}
