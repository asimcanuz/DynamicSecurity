package org.asodev.dynamicsecurity.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.asodev.dynamicsecurity.service.JwtService;
import org.asodev.dynamicsecurity.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        log.info("Processing JWT authentication filter");
        String token = parseJwt(request);
        String userName = null;

        if (token != null) {
            userName = jwtService.extractUser(token);
        }

        log.info("Extracted username: " + userName);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userService.loadUserByUsername(userName);
            if (Boolean.TRUE.equals(jwtService.validateToken(token, user))) {
                log.info("token validated for user: {}", userName);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
                List<String> roles = authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                log.info("User '{}' authorities: {}", userName, roles);

                try {
                    List<String> perms = jwtService.extractPermissions(token);
                    if (perms != null) {
                        log.info("Permissions from token for user '{}': {}", userName, perms);
                    }
                } catch (Exception e) {
                    log.debug("Could not extract permissions from token: {}", e.getMessage());
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}