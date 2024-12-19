package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public class PermissionRepositoryImpl implements CustomRepository<Permission, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Permission findOrSave(Permission entity) {
        try {
            var permission = entityManager
                    .createQuery("SELECT p FROM Permission p WHERE p.name = :name", Permission.class)
                    .setParameter("name", entity.getName())
                    .getSingleResult();

            if (isNull(permission)) {
                entityManager.persist(entity);
                return entity;
            } else {
                return permission;
            }

        } catch (jakarta.persistence.NoResultException e) {
            return null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

    }

    @Override
    public List<Permission> findOrBatchSave(List<Permission> entities) {
        return removeDuplication(entities, Permission::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
