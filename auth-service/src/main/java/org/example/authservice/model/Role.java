package org.example.authservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User roles for role-based access control")
public enum Role {
    @Schema(description = "Regular user")
    USER,

    @Schema(description = "System administrator")
    ADMIN,

    @Schema(description = "Emergency responder / authority")
    RESPONDER
}
