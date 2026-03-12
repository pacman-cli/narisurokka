package org.example.userservice.event;

import java.util.UUID;

public record UserRegisteredEvent(
    UUID userId,
    String email,
    String phone) {
}
