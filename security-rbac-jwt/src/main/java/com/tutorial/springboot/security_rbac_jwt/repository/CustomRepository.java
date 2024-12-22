package com.tutorial.springboot.security_rbac_jwt.repository;

import java.util.List;

public interface CustomRepository<ENTITY, ID> {
    ENTITY findOrCreate(ENTITY entity);

    List<ENTITY> findOrCreateAll(List<ENTITY> entities);
}
