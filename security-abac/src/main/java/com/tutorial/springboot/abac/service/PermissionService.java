package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.convertor.PermissionConverter;
import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.model.Permission;
import com.tutorial.springboot.abac.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionDto> getAll() {
        return permissionRepository.findAll().stream()
                .map(PermissionConverter::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PermissionDto> getById(Long id) {
        return permissionRepository.findById(id).map(PermissionConverter::toDto);
    }

    public PermissionDto create(PermissionDto permissionDto) {
        Permission permission = PermissionConverter.toEntity(permissionDto);
        permission = permissionRepository.save(permission);
        return PermissionConverter.toDto(permission);
    }

    public Optional<PermissionDto> update(Long id, PermissionDto permissionDto) {
        if (permissionRepository.existsById(id)) {
            Permission permission = PermissionConverter.toEntity(permissionDto);
            permission.setId(id);
            permission = permissionRepository.save(permission);
            return Optional.of(PermissionConverter.toDto(permission));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
