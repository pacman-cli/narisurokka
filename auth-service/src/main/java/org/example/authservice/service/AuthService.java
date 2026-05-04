package org.example.authservice.service;

import java.util.UUID;

import org.example.authservice.authentication.AuthResponse;
import org.example.authservice.authentication.LoginRequest;
import org.example.authservice.registration.RegisterRequest;

public interface AuthService {
  AuthResponse register(RegisterRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refreshToken(String refreshToken);

  void logout(UUID userId);
}
