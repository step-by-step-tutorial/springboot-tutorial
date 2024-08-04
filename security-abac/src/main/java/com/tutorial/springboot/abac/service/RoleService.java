package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.convertor.RoleConverter;
import com.tutorial.springboot.abac.dto.RoleDto;
import com.tutorial.springboot.abac.model.Role;
import com.tutorial.springboot.abac.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream()
                .map(RoleConverter::toDto)
                .collect(Collectors.toList());
    }

    public Optional<RoleDto> getById(Long id) {
        return roleRepository.findById(id).map(RoleConverter::toDto);
    }

    public RoleDto create(RoleDto roleDto) {
        Role role = RoleConverter.toEntity(roleDto);
        role = roleRepository.save(role);
        return RoleConverter.toDto(role);
    }

    public Optional<RoleDto> update(Long id, RoleDto roleDto) {
        if (roleRepository.existsById(id)) {
            Role role = RoleConverter.toEntity(roleDto);
            role.setId(id);
            role = roleRepository.save(role);
            return Optional.of(RoleConverter.toDto(role));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
