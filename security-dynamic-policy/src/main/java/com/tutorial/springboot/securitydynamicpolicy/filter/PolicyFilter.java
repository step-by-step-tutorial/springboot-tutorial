package com.tutorial.springboot.securitydynamicpolicy.filter;

import com.tutorial.springboot.securitydynamicpolicy.service.impl.PolicyEngineService;
import com.tutorial.springboot.securitydynamicpolicy.service.evaluator.CriteriaEvaluator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PolicyFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(CriteriaEvaluator.class);

    private final PolicyEngineService policyEngineService;

    public PolicyFilter(PolicyEngineService policyEngineService) {
        this.policyEngineService = policyEngineService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            policyEngineService.execute(request, response);
        } catch (Exception e) {
            logger.error("Unable to execute policy due to: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
