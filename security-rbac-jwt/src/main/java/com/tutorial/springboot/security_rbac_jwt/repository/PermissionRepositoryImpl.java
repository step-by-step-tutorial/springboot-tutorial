package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static java.util.stream.Collectors.toList;

public class PermissionRepositoryImpl implements CustomRepository<Permission, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Permission findOrSave(Permission entity) {
        try {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(Permission.class);
            var root = cq.from(Permission.class);
            cq.select(root).where(cb.equal(root.get("name"), entity.getName()));
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            entityManager.persist(entity);
            return entity;
        }
    }

    @Override
    public List<Permission> findOrSaveAll(List<Permission> entities) {
        return removeDuplication(entities, Permission::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
