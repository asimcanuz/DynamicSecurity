package org.asodev.dynamicsecurity.service;

import org.asodev.dynamicsecurity.dto.CreateRoleRequest;
import org.asodev.dynamicsecurity.model.Permission;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.repository.PermissionRepository;
import org.asodev.dynamicsecurity.repository.RoleRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void create(CreateRoleRequest roleRequest) {
        Role role = Role.builder().name(roleRequest.name()).build();
        roleRepository.save(role);
    }

    private Role getRoleById(Long role) {
        return roleRepository.findById(role)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + role));
    }

    private Permission getPermissionById(Long permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + permissionId));
    }

    public void insertPermission(Long roleId, Long permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = getPermissionById(permissionId);
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    public void deleteRole(Long roleId) {
        Role role = getRoleById(roleId);

        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            throw new IllegalArgumentException("Role has associated users and cannot be deleted");
        }

        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            throw new IllegalArgumentException("Role has associated permissions and cannot be deleted");
        }

        roleRepository.delete(role);

    }

}
