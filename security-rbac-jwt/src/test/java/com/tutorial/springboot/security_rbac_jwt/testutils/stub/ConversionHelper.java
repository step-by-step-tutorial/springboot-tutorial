package com.tutorial.springboot.security_rbac_jwt.testutils.stub;

public record ConversionHelper<ENTITY, DTO>(VarcharHelper<ENTITY> entity, VarcharHelper<DTO> dto) {

}