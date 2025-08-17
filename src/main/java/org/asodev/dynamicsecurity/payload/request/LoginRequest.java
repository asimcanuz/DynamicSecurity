package org.asodev.dynamicsecurity.payload.request;

import org.asodev.dynamicsecurity.interceptor.ValidPassword;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String username,
        @NotBlank
        @ValidPassword
        String password) {

}
