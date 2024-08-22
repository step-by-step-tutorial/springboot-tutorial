package com.tutorial.springboot.rbac.test_utils.assistant;

public class AssistantResultHelper<ENTITY, DTO> {
    public ENTITY asEntity;
    public DTO asDto;

    public AssistantResultHelper(ENTITY entity, DTO dto) {
        this.asEntity = entity;
        this.asDto = dto;
    }
}