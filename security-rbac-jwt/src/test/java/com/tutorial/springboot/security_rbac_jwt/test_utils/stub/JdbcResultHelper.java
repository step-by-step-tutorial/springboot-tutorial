package com.tutorial.springboot.security_rbac_jwt.test_utils.stub;

public record JdbcResultHelper<ENTITY, DTO>(StubHelper<ENTITY> entity, StubHelper<DTO> dto) {

}