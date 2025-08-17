package org.asodev.dynamicsecurity.dto;

import org.asodev.dynamicsecurity.interceptor.ValidPassword;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String username,
        @NotBlank
        @ValidPassword
        String password) {

}
