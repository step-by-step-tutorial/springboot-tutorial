package com.tutorial.springboot.securitydynamicpolicy.dto;

public class PolicyDto extends AbstractDto<Long, PolicyDto> {

    private String type;
    private String criteria;
    private String identifier;
    private String condition;
    private String criteriaScript;
    private String actionName;
    private String actionScript;
    private String actionParams;

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

    public String getCriteriaScript() {
        return criteriaScript;
    }

    public void setCriteriaScript(String criteriaScript) {
        this.criteriaScript = criteriaScript;
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
