package com.tutorial.springboot.securitydynamicpolicy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

import java.util.Objects;

/**
 * type: BLACKLIST, WHITELIST, etc
 * criteria: Type of criteria, e.g., IP, USERNAME, HEADER
 * criteriaScript: Groovy script for extracting criteria
 * identifier: Identifier to match, if applicable
 * condition: SpEL or expression
 * actionName: Descriptive name of the action
 * actionScript: Groovy script for the action
 * actionParams: Optional JSON parameters for the action
 */

@Entity
public class Policy extends AbstractEntity<Long, Policy> {

    private String type;

    private String criteria;

    @Lob
    private String criteriaScript;

    private String identifier;

    private String condition;

    private String actionName;

    @Lob
    private String actionScript;

    private String actionParams;

    @Override
    public Policy updateFrom(Policy newOne) {
        super.updateFrom(newOne);
        this.type = newOne.type;
        this.criteria = newOne.criteria;
        this.criteriaScript = newOne.criteriaScript;
        this.identifier = newOne.identifier;
        this.condition = newOne.condition;
        this.actionName = newOne.actionName;
        this.actionScript = newOne.actionScript;
        this.actionParams = newOne.actionParams;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return Objects.equals(type, policy.type)
               && Objects.equals(criteria, policy.criteria)
               && Objects.equals(identifier, policy.identifier)
               && Objects.equals(actionName, policy.actionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, criteria, identifier, actionName);
    }

    @Override
    public String toString() {
        return "Policy{" +
               "type='" + type + '\'' +
               ", criteria='" + criteria + '\'' +
               ", identifier='" + identifier + '\'' +
               ", actionName='" + actionName + '\'' +
               '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteriaScript() {
        return criteriaScript;
    }

    public void setCriteriaScript(String criteriaScript) {
        this.criteriaScript = criteriaScript;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionScript() {
        return actionScript;
    }

    public void setActionScript(String actionScript) {
        this.actionScript = actionScript;
    }

    public String getActionParams() {
        return actionParams;
    }

    public void setActionParams(String actionParams) {
        this.actionParams = actionParams;
    }
}
