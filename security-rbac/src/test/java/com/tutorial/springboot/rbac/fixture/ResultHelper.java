package com.tutorial.springboot.rbac.fixture;

public class ResultHelper<ENTITY, DTO> {
    public ENTITY asEntity;
    public DTO asDto;

    public ResultHelper(ENTITY entity, DTO dto) {
        this.asEntity = entity;
        this.asDto = dto;
    }
}