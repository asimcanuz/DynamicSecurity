package org.asodev.dynamicsecurity.service;

import org.asodev.dynamicsecurity.payload.request.LoginRequest;
import org.asodev.dynamicsecurity.payload.request.SignupRequest;
import org.asodev.dynamicsecurity.payload.response.TokenResponse;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.model.User;
import org.asodev.dynamicsecurity.repository.RoleRepository;
import org.asodev.dynamicsecurity.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService userService;

  public void registerUser(SignupRequest request) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(request.username()))) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (Boolean.TRUE.equals(userRepository.existsByEmail(request.email()))) {
      throw new IllegalArgumentException("Email already exists");
    }

    Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new IllegalArgumentException("Role not found"));

    User newUser = User.builder()
        .firstName(request.firstName())
        .lastName(request.lastname())
        .email(request.email())
        .phoneNumber(request.phoneNumber())
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .accountNonExpired(true)
        .credentialsNonExpired(true)
        .isEnabled(true)
        .accountNonLocked(true)
        .role(userRole)
        .build();
    userRepository.save(newUser);

  }

  public TokenResponse loginUser(LoginRequest request) {

    User user = userService.getByUsername(request.username())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid password");
    }

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    if (authentication.isAuthenticated()) {
      String token = jwtService.generateToken(request.username(), user.getRole());
      return new TokenResponse(token);
    } else {
      throw new IllegalArgumentException("Authentication failed");
    }
  }

  public void logoutUser() {
        SecurityContextHolder.clearContext();
        // Additional logout logic can be added here if needed
    }
}
