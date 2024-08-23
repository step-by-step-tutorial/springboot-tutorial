package com.tutorial.springboot.rbac.test_utils.stub;

public record ResultHelper<ENTITY, DTO>(StubHelper<ENTITY> entity, StubHelper<DTO> dto) {

}