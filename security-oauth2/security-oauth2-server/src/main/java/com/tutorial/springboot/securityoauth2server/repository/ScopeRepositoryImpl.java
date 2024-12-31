package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static com.tutorial.springboot.securityoauth2server.util.CollectionUtils.removeDuplication;
import static java.util.stream.Collectors.toList;

public class ScopeRepositoryImpl implements CustomRepository<Scope, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Scope findOrSave(Scope entity) {
        try {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(Scope.class);
            var root = cq.from(Scope.class);
            cq.select(root).where(cb.equal(root.get("name"), entity.getName()));
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            entityManager.persist(entity);
            return entity;
        }
    }

    @Override
    public List<Scope> findOrSaveAll(List<Scope> entities) {
        return removeDuplication(entities, Scope::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
