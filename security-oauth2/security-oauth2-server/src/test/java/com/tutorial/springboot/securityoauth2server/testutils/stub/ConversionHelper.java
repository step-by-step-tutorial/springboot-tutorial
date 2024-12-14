package com.tutorial.springboot.securityoauth2server.testutils.stub;

public record ConversionHelper<ENTITY, DTO>(VarcharHelper<ENTITY> entity, VarcharHelper<DTO> dto) {

}