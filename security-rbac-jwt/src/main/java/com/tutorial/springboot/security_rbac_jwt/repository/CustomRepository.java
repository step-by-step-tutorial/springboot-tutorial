package com.tutorial.springboot.security_rbac_jwt.repository;

import java.util.List;

public interface CustomRepository<ENTITY, ID> {
    ENTITY findOrSave(ENTITY entity);

    List<ENTITY> findOrBatchSave(List<ENTITY> entities);
}
