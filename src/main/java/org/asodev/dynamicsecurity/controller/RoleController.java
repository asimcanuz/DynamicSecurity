package org.asodev.dynamicsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.payload.request.CreateRoleRequest;
import org.asodev.dynamicsecurity.service.RoleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('role.read')")
    public ResponseEntity<List<Role>> getAll(){
        return ResponseEntity.ok(roleService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role.create')")
    public ResponseEntity<Void> createRole(CreateRoleRequest roleRequest) {
        roleService.create(roleRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize(" hasAuthority('role.read')")
    public ResponseEntity<Role> getById(@PathVariable Long id){
    Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

}
