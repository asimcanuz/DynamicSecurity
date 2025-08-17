package org.asodev.dynamicsecurity.service;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.cache.RoleCacheManager;
import org.asodev.dynamicsecurity.model.Permission;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.payload.request.CreateRoleRequest;
import org.asodev.dynamicsecurity.repository.PermissionRepository;
import org.asodev.dynamicsecurity.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleCacheManager roleCacheManager;

    public void create(CreateRoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.name())) {
            throw new IllegalArgumentException("Role already exists with name: " + roleRequest.name());
        }

        Role role = Role.builder().name(roleRequest.name()).build();
        roleRepository.save(role);
        roleCacheManager.evictRolesCache();
    }

    public List<Role> getAll() {
        List<Role> cachedRoles = roleCacheManager.getRolesFromCache();
        if (!cachedRoles.isEmpty()) {
            return cachedRoles;
        }

        List<Role> roles = roleRepository.findAll();
        roleCacheManager.refreshRolesCache(roles);
        return roles;
    }

    public Role getRoleById(Long roleId) {

        Role role = roleCacheManager.getRoleByIdFromCache(roleId).orElse(null);
        if (Objects.nonNull(role)) {
            return role;
        }

        Role role1 = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleId));
        roleCacheManager.cacheRoleById(role1);

        return role1;
    }
}
