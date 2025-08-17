package org.asodev.dynamicsecurity;

import java.util.HashSet;
import java.util.Set;

import org.asodev.dynamicsecurity.model.Permission;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.repository.PermissionRepository;
import org.asodev.dynamicsecurity.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class DynamicSecurityApplication {
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	public static void main(String[] args) {
		SpringApplication.run(DynamicSecurityApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			log.info("Dynamic Security Application has started successfully!");

			Permission godPermission = new Permission();
			godPermission.setCode("god");
			godPermission.setDescription("GOD");
			permissionRepository.save(godPermission);

			Set<Permission> permissions = new HashSet<>();
			permissions.add(godPermission);

			Role role = new Role();
			role.setName("ROLE_USER");
			role.setPermissions(permissions);
			roleRepository.save(role);

			Role adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
			adminRole.setPermissions(permissions);
			roleRepository.save(adminRole);

			Role superAdminRole = new Role();
			superAdminRole.setName("ROLE_SUPER_ADMIN");
			superAdminRole.setPermissions(permissions);
			roleRepository.save(superAdminRole);

			log.info("Roles have been initialized successfully!");

		};
	}

}
