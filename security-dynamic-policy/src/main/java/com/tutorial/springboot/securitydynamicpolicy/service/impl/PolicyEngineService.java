package com.tutorial.springboot.securitydynamicpolicy.service.impl;

import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import com.tutorial.springboot.securitydynamicpolicy.repository.PolicyRepository;
import com.tutorial.springboot.securitydynamicpolicy.service.evaluator.ActionEvaluator;
import com.tutorial.springboot.securitydynamicpolicy.service.evaluator.ConditionEvaluator;
import com.tutorial.springboot.securitydynamicpolicy.service.evaluator.CriteriaEvaluator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class PolicyEngineService {

    private final PolicyRepository policyRepository;
    private final CriteriaEvaluator criteriaEvaluator;
    private final ConditionEvaluator conditionEvaluator;
    private final ActionEvaluator actionEvaluator;

    public PolicyEngineService(
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
