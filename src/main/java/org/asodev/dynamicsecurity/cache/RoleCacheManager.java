package org.asodev.dynamicsecurity.cache;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.model.Role;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleCacheManager {

    private final CacheManager cacheManager;

    private static final String CACHE_NAME = "roles";
    private static final String ALL_ROLES_KEY = "allRoles";
    private static final String ROLE_KEY_PREFIX = "role:";

    /**
     * Retrieves all roles from cache
     */
    public List<Role> getRolesFromCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return Collections.emptyList();
        }

        Cache.ValueWrapper valueWrapper = cache.get(ALL_ROLES_KEY);
        if (valueWrapper == null) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<Role> roles = (List<Role>) valueWrapper.get();
        return roles != null ? roles : Collections.emptyList();
    }

    /**
     * Updates the cache with the provided roles
     */
    public void refreshRolesCache(List<Role> roles) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(ALL_ROLES_KEY, roles);
        }
    }

    /**
     * Removes all roles from cache
     */
    public void evictRolesCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.evict(ALL_ROLES_KEY);
        }
    }

    /**
     * Gets a specific role by id from cache
     */
    public Optional<Role> getRoleByIdFromCache(Long roleId) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return Optional.empty();
        }

        String key = ROLE_KEY_PREFIX + roleId;
        Cache.ValueWrapper valueWrapper = cache.get(key);

        return Optional.ofNullable(valueWrapper)
                .map(wrapper -> (Role) wrapper.get());
    }

    /**
     * Stores a specific role in cache
     */
    public void cacheRoleById(Role role) {
        if (role == null || role.getId() == null) {
            return;
        }

        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            String key = ROLE_KEY_PREFIX + role.getId();
            cache.put(key, role);
        }
    }

    /**
     * Evicts a specific role from cache
     */
    public void evictRoleByIdFromCache(Long roleId) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            String key = ROLE_KEY_PREFIX + roleId;
            cache.evict(key);
        }
    }

    /**
     * Clears the entire roles cache
     */
    public void clearAllRolesCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
        }
    }
}