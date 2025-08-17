package org.asodev.dynamicsecurity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asodev.dynamicsecurity.service.AuthService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Slf4j
@EnableCaching
@RequiredArgsConstructor
public class DynamicSecurityApplication  {
	 private final AuthService authService;
	public static void main(String[] args) {
		SpringApplication.run(DynamicSecurityApplication.class, args);
		log.info("Application Started");
	}



}
