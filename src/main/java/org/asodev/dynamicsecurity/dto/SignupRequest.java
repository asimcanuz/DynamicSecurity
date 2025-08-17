package org.asodev.dynamicsecurity.dto;

import org.asodev.dynamicsecurity.interceptor.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignupRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastname,
        @NotBlank
        String username,
        @NotBlank
        @ValidPassword
        String password,
        @NotBlank
        @Email
        String email,
        @Number
        String phoneNumber) {

}
