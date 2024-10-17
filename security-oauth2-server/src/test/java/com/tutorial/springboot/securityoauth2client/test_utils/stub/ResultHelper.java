package com.tutorial.springboot.securityoauth2client.test_utils.stub;

public record ResultHelper<ENTITY, DTO>(StubHelper<ENTITY> entity, StubHelper<DTO> dto) {

}