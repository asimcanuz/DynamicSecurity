package org.asodev.dynamicsecurity.payload.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank
        String name
        ) {}
