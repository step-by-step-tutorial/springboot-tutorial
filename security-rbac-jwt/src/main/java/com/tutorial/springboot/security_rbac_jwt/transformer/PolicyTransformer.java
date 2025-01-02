package com.tutorial.springboot.security_rbac_jwt.transformer;

import com.tutorial.springboot.security_rbac_jwt.dto.PolicyDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Policy;
import org.springframework.stereotype.Component;

@Component
public class PolicyTransformer extends AbstractTransformer<Long, Policy, PolicyDto> {

    @Override
    protected void copyEntityToDto(Policy entity, PolicyDto dto) {
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setCriteria(entity.getCriteria());
        dto.setIdentifier(entity.getIdentifier());
        dto.setCondition(entity.getCondition());
        dto.setCriteriaScript(entity.getCriteriaScript());
        dto.setActionName(entity.getActionName());
        dto.setActionScript(entity.getActionScript());
        dto.setActionParams(entity.getActionParams());
    }

    @Override
    protected void copyDtoToEntity(PolicyDto dto, Policy entity) {
        entity.setId(dto.getId());
        entity.setType(dto.getType());
        entity.setCriteria(dto.getCriteria());
        entity.setIdentifier(dto.getIdentifier());
        entity.setCondition(dto.getCondition());
        entity.setCriteriaScript(dto.getCriteriaScript());
        entity.setActionName(dto.getActionName());
        entity.setActionScript(dto.getActionScript());
        entity.setActionParams(dto.getActionParams());
    }
}
