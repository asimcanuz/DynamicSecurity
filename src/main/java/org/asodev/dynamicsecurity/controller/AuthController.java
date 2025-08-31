package org.asodev.dynamicsecurity.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.payload.request.LoginRequest;
import org.asodev.dynamicsecurity.payload.request.RefreshTokenRequest;
import org.asodev.dynamicsecurity.payload.request.SignupRequest;
import org.asodev.dynamicsecurity.payload.response.TokenResponse;
import org.asodev.dynamicsecurity.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public String registerUser(@Valid @RequestBody SignupRequest request) {
    authService.register(request);
    return "User registered successfully";
  }



  @PostMapping("/login")
  public TokenResponse loginUser(@Valid @RequestBody LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/logout")
  public String logoutUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    authService.logout(authentication);

    return "User logged out successfully";
  }

  @PostMapping("/refresh")
  public TokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
    return authService.refreshToken(request);
  }

  @PostMapping("/deactive-account")
  public String deactiveAccount(@RequestBody(required = true) String username) {
    authService.deactivate(username);
    return "User account deactivated successfully";
  }

  @PostMapping("/active-account")
    public String activeAccount(@RequestBody(required = true) String username) {
        authService.activate(username);
        return "User account activated successfully";
  }

}