package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.GrantType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static com.tutorial.springboot.securityoauth2server.util.CollectionUtils.removeDuplication;
import static java.util.stream.Collectors.toList;

public class GrantTypeRepositoryImpl implements CustomRepository<GrantType, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GrantType findOrSave(GrantType entity) {
        try {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(GrantType.class);
            var root = cq.from(GrantType.class);
            cq.select(root).where(cb.equal(root.get("name"), entity.getName()));
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            entityManager.persist(entity);
            return entity;
        }
    }

    @Override
    public List<GrantType> findOrSaveAll(List<GrantType> entities) {
        return removeDuplication(entities, GrantType::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
