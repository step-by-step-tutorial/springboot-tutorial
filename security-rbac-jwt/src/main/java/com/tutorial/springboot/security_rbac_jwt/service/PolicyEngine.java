package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.entity.Policy;
import com.tutorial.springboot.security_rbac_jwt.repository.PolicyRepository;
import com.tutorial.springboot.security_rbac_jwt.service.evaluator.ActionEvaluator;
import com.tutorial.springboot.security_rbac_jwt.service.evaluator.ConditionEvaluator;
import com.tutorial.springboot.security_rbac_jwt.service.evaluator.CriteriaEvaluator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class PolicyEngine {

    private final PolicyRepository policyRepository;
    private final CriteriaEvaluator criteriaEvaluator;
    private final ConditionEvaluator conditionEvaluator;
    private final ActionEvaluator actionEvaluator;

    public PolicyEngine(
            PolicyRepository policyRepository,
            CriteriaEvaluator criteriaEvaluator,
            ConditionEvaluator conditionEvaluator,
            ActionEvaluator actionEvaluator) {
        this.policyRepository = policyRepository;
        this.criteriaEvaluator = criteriaEvaluator;
        this.conditionEvaluator = conditionEvaluator;
        this.actionEvaluator = actionEvaluator;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) {
        var policies = policyRepository.findAll();

        for (Policy policy : policies) {
            var criteriaResult = criteriaEvaluator.evaluateCriteriaScript(policy.getCriteriaScript(), policy, request);
            var conditionMatched = conditionEvaluator.evaluateCondition(policy.getCondition(), criteriaResult, policy, request);

            if (conditionMatched) {
                actionEvaluator.executeActionScript(policy.getActionScript(), policy, request, response);
                return;
            }
        }
    }
}
