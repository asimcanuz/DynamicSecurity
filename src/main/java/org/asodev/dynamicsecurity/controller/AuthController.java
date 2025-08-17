package org.asodev.dynamicsecurity.controller;

import org.asodev.dynamicsecurity.dto.LoginRequest;
import org.asodev.dynamicsecurity.dto.SignupRequest;
import org.asodev.dynamicsecurity.dto.TokenResponse;
import org.asodev.dynamicsecurity.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public String registerUser(@RequestBody SignupRequest request) {
    authService.registerUser(request);
    return "User registered successfully";
  }

  @PostMapping("/login")
  public TokenResponse loginUser(@RequestBody LoginRequest request) {
    return authService.loginUser(request);
  }
}
