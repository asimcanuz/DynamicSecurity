package org.asodev.dynamicsecurity.repository;

import java.util.Optional;

import org.asodev.dynamicsecurity.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
  Optional<Permission> findByCode(String code);
}
