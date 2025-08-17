package org.asodev.dynamicsecurity.controller;

import jakarta.validation.Valid;
import org.asodev.dynamicsecurity.payload.request.LoginRequest;
import org.asodev.dynamicsecurity.payload.request.SignupRequest;
import org.asodev.dynamicsecurity.payload.response.TokenResponse;
import org.asodev.dynamicsecurity.service.AuthService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public String registerUser(@Valid @RequestBody SignupRequest request) {
    authService.registerUser(request);
    return "User registered successfully";
  }

  @PostMapping("/login")
  public TokenResponse loginUser(@Valid @RequestBody LoginRequest request) {
    return authService.loginUser(request);
  }

    @PostMapping("/logout")
    public String logoutUser() {
    authService.logoutUser();
    return "User logged out successfully";
    }
}
