package com.tutorial.springboot.securitydynamicpolicy.service.evaluator;

import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActionEvaluator {

    private final Logger logger = LoggerFactory.getLogger(CriteriaEvaluator.class);

    public void executeActionScript(String script, Policy policy, HttpServletRequest request, HttpServletResponse response) {
        if (script == null || script.isEmpty()) {
            return;
        }

        try {
            var binding = new Binding();
            binding.setVariable("request", request);
            binding.setVariable("response", response);
            binding.setVariable("policy", policy);

            var shell = new GroovyShell(binding);
            shell.evaluate(script);
        } catch (Exception e) {
            logger.error("Action script execution failed: {}", e.getMessage());
        }
    }
}
