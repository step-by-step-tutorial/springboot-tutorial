package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static java.util.stream.Collectors.toList;

public class RoleRepositoryImpl implements CustomRepository<Role, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Role findOrSave(Role entity) {
        var permissions = permissionRepository.findOrBatchSave(entity.getPermissions());

        try {
            var role = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", entity.getName())
                    .getSingleResult();

            role.getPermissions().addAll(permissions);
            role.setPermissions(removeDuplication(role.getPermissions(), Permission::getName).collect(toList()));
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
    public List<Role> findOrBatchSave(List<Role> entities) {
        return removeDuplication(entities, Role::getName)
                .map(entity -> findOrSave(entity))
                .collect(toList());
    }
}
