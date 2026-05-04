package org.example.authservice.service;

import java.util.UUID;

import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.RegisterRequest;

public interface AuthService {
  AuthResponse register(RegisterRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refreshToken(String refreshToken);

  void logout(UUID userId);
}
