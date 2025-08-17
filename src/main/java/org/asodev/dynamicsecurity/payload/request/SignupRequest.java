package org.asodev.dynamicsecurity.payload.request;

import org.asodev.dynamicsecurity.interceptor.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.asodev.dynamicsecurity.interceptor.ValidPhone;

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
        @ValidPhone
        String phoneNumber) {

}
