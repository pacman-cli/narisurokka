package org.example.sosservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of an SOS case")
public enum SosStatus {
    @Schema(description = "SOS is currently active and awaiting response")
    ACTIVE,

    @Schema(description = "SOS was cancelled by the user")
    CANCELLED,

    @Schema(description = "SOS was resolved by responders")
    RESOLVED
}

// @Schema -> Swagger
