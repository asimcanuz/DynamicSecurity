package org.asodev.dynamicsecurity.service;

import lombok.RequiredArgsConstructor;
import org.asodev.dynamicsecurity.model.Role;
import org.asodev.dynamicsecurity.model.User;
import org.asodev.dynamicsecurity.payload.request.LoginRequest;
import org.asodev.dynamicsecurity.payload.request.RefreshTokenRequest;
import org.asodev.dynamicsecurity.payload.request.SignupRequest;
import org.asodev.dynamicsecurity.payload.response.TokenResponse;
import org.asodev.dynamicsecurity.payload.response.TokenVerificationResponse;
import org.asodev.dynamicsecurity.repository.RoleRepository;
import org.asodev.dynamicsecurity.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService userService;
  private final VerificationTokenService verificationTokenService;

  @Transactional
  public void register(SignupRequest request) {
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
        .isEnabled(false)
        .accountNonLocked(true)
        .role(userRole)
        .build();

    userRepository.save(newUser);

    verificationTokenService.sendVerificationEmail(newUser);

  }

  @Transactional
  public TokenResponse login(LoginRequest request) {

    User user = userService.getByUsername(request.username())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid password");
    }

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    if (authentication.isAuthenticated()) {

      jwtService.revokeAllUserTokens(user.getId());
      String accessToken = jwtService.generateToken(request.username(), user.getRole());
      String refreshToken = jwtService.generateRefreshToken(request.username());

      return new TokenResponse(
              accessToken,
              refreshToken,
              jwtService.getAccessTokenExpirationInSeconds()
      );
    } else {
      throw new IllegalArgumentException("Authentication failed");
    }
  }

  @Transactional
  public TokenResponse refreshToken(RefreshTokenRequest request) {
    String refreshToken = request.refreshToken();

    if (!jwtService.validateRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    String username = jwtService.extractUser(refreshToken);
    User user = userService.getByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    jwtService.revokeAllUserTokens(user.getId());

    String newAccessToken = jwtService.generateToken(username, user.getRole());
    String newRefreshToken = jwtService.generateRefreshToken(username);

    return new TokenResponse(
            newAccessToken,
            newRefreshToken,
            jwtService.getAccessTokenExpirationInSeconds()
    );
  }

  public void logout(Authentication authentication) {
    if (authentication != null) {
      User user = (User) authentication.getPrincipal();
      jwtService.revokeAllUserTokens(user.getId());
      SecurityContextHolder.clearContext();
    }
  }

  public void deactivate(String username) {
    User user = userService.getByUsername(username).orElseThrow(()-> new IllegalArgumentException("User not found"));
    user.setEnabled(false);
    userRepository.save(user);
    jwtService.revokeAllUserTokens(user.getId());
    SecurityContextHolder.clearContext();
  }



  public void activate(String username){
    User user = userService.getByUsername(username).orElseThrow(()-> new IllegalArgumentException("User not found"));
    user.setEnabled(true);
    userRepository.save(user);
  }
}
