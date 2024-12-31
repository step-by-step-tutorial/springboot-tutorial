package com.tutorial.springboot.securityoauth2server.repository;

import java.util.List;

public interface CustomRepository<ENTITY, ID> {

    ENTITY findOrSave(ENTITY entity);

    List<ENTITY> findOrSaveAll(List<ENTITY> entities);
}
