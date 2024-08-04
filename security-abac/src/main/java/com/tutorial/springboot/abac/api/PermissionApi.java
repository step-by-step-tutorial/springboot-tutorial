package com.tutorial.springboot.abac.api;

import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionApi {

    private final PermissionService permissionService;

    public PermissionApi(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PermissionDto> getAll() {
        return permissionService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PermissionDto> getById(@PathVariable Long id) {
        return ResponseEntity.of(permissionService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public PermissionDto create(@RequestBody PermissionDto permissionDto) {
        return permissionService.create(permissionDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PermissionDto> update(@PathVariable Long id, @RequestBody PermissionDto permissionDto) {
        return ResponseEntity.of(permissionService.update(id, permissionDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
