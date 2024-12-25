package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.compareCollections;
import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static java.util.stream.Collectors.toList;

public class RoleRepositoryImpl implements CustomRepository<Role, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Role findOrSave(Role entity) {
        var permissions = permissionRepository.findOrSaveAll(entity.getPermissions());

        try {
            var cb = entityManager.getCriteriaBuilder();
            var cq = cb.createQuery(Role.class);
            var root = cq.from(Role.class);
            cq.select(root).where(cb.equal(root.get("name"), entity.getName()));
            var role = entityManager.createQuery(cq).getSingleResult();

            var compared = compareCollections(role.getPermissions(), permissions);
            role.getPermissions().removeAll(compared.deletionItems());
            role.getPermissions().addAll(compared.newItems());

            entityManager.persist(role);
            return role;

        } catch (Exception e) {
            entity.getPermissions().clear();
            entity.getPermissions().addAll(permissions);
            entityManager.persist(entity);
            return entity;
        }
    }

    @Override
    public List<Role> findOrSaveAll(List<Role> entities) {
        return removeDuplication(entities, Role::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
