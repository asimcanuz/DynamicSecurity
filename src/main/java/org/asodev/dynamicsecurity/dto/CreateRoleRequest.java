package org.asodev.dynamicsecurity.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank
        String name
        ) {

}
