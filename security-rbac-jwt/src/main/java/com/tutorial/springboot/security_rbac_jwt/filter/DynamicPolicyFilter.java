package com.tutorial.springboot.security_rbac_jwt.filter;

import com.tutorial.springboot.security_rbac_jwt.service.PolicyEngine;
import com.tutorial.springboot.security_rbac_jwt.service.evaluator.CriteriaEvaluator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DynamicPolicyFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(CriteriaEvaluator.class);

    private final PolicyEngine policyEngine;

    public DynamicPolicyFilter(PolicyEngine policyEngine) {
        this.policyEngine = policyEngine;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            policyEngine.execute(request, response);
        } catch (Exception e) {
            logger.error("Unable to execute policy due to: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
