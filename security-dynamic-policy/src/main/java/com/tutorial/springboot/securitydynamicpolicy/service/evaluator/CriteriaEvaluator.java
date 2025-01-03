package com.tutorial.springboot.securitydynamicpolicy.service.evaluator;

import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CriteriaEvaluator {

    private final Logger logger = LoggerFactory.getLogger(CriteriaEvaluator.class);

    public Object evaluateCriteriaScript(String script, Policy policy, HttpServletRequest request) {
        if (script == null || script.isEmpty()) {
            return null;
        }

        try {
            var binding = new Binding();
            binding.setVariable("request", request);
            binding.setVariable("policy", policy);

            var shell = new GroovyShell(binding);
            return shell.evaluate(script);
        } catch (Exception e) {
            logger.error("Criteria script evaluation failed: {}", e.getMessage());
            return null;
        }
    }
}
