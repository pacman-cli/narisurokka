package org.example.authservice.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.RegisterRequest;
import org.example.authservice.exception.AuthenticationFailedException;
import org.example.authservice.exception.InvalidTokenException;
import org.example.authservice.exception.UserAlreadyExistsException;
import org.example.authservice.kafka.AuthEventProducer;
import org.example.authservice.model.AuthUser;
import org.example.authservice.model.RefreshToken;
import org.example.authservice.model.Role;
import org.example.authservice.repository.AuthUserRepository;
import org.example.authservice.repository.RefreshTokenRepository;
import org.example.authservice.security.JwtTokenProvider;
import org.example.authservice.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final AuthUserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final AuthEventProducer authEventProducer;

  private static final int MAX_FAILED_ATTEMPTS = 5;

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    // Check for duplicate email
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExistsException("Email already registered: " + request.getEmail());
    }

    // Check for duplicate phone (if provided)
    if (request.getPhone() != null && !request.getPhone().isBlank()
        && userRepository.existsByPhone(request.getPhone())) {
      throw new UserAlreadyExistsException("Phone already registered: " + request.getPhone());
    }

    // Create user
    AuthUser user = AuthUser.builder()
        .email(request.getEmail())
        .phone(request.getPhone())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .enabled(true)
        .locked(false)
        .build();
    userRepository.save(user);

    // Generate tokens
    String accessToken = jwtTokenProvider.generateAccessToken(
        user.getId(), user.getEmail(), user.getRole().name());
    RefreshToken refreshToken = createRefreshToken(user);

    // Publish Kafka event
    authEventProducer.publishUserRegistered(user.getId(), user.getEmail(), user.getRole().name());

    log.info("User registered: userId={}, email={}", user.getId(), user.getEmail());

    return buildAuthResponse(user, accessToken, refreshToken.getToken());
  }

  @Override
  @Transactional
  public AuthResponse login(LoginRequest request) {
    AuthUser user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

    // Check if account is locked
    if (user.isLocked()) {
      throw new AuthenticationFailedException("Account is locked due to too many failed login attempts");
    }

    // Check if account is enabled
    if (!user.isEnabled()) {
      throw new AuthenticationFailedException("Account is disabled");
    }

    // Verify password
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      // Increment failed attempts
      user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
      if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
        user.setLocked(true);
        log.warn("Account locked for userId={} after {} failed attempts",
            user.getId(), MAX_FAILED_ATTEMPTS);
      }
      userRepository.save(user);
      throw new AuthenticationFailedException("Invalid email or password");
    }

    // Reset failed attempts on successful login
    user.setFailedLoginAttempts(0);
    userRepository.save(user);

    // Revoke existing refresh tokens
    refreshTokenRepository.revokeAllByUserId(user.getId());

    // Generate new tokens
    String accessToken = jwtTokenProvider.generateAccessToken(
        user.getId(), user.getEmail(), user.getRole().name());
    RefreshToken refreshToken = createRefreshToken(user);

    // Publish Kafka event
    authEventProducer.publishUserLoggedIn(user.getId(), user.getEmail());

    log.info("User logged in: userId={}, email={}", user.getId(), user.getEmail());

    return buildAuthResponse(user, accessToken, refreshToken.getToken());
  }

  @Override
  @Transactional
  public AuthResponse refreshToken(String token) {
    RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
        .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

    if (refreshToken.isRevoked()) {
      throw new InvalidTokenException("Refresh token has been revoked");
    }

    if (refreshToken.isExpired()) {
      refreshToken.setRevoked(true);
      refreshTokenRepository.save(refreshToken);
      throw new InvalidTokenException("Refresh token has expired");
    }

    AuthUser user = refreshToken.getUser();

    // Revoke old refresh token
    refreshToken.setRevoked(true);
    refreshTokenRepository.save(refreshToken);

    // Generate new tokens (token rotation)
    String accessToken = jwtTokenProvider.generateAccessToken(
        user.getId(), user.getEmail(), user.getRole().name());
    RefreshToken newRefreshToken = createRefreshToken(user);

    log.info("Token refreshed for userId={}", user.getId());

    return buildAuthResponse(user, accessToken, newRefreshToken.getToken());
  }

  @Override
  @Transactional
  public void logout(UUID userId) {
    refreshTokenRepository.revokeAllByUserId(userId);
    authEventProducer.publishUserLoggedOut(userId);
    log.info("User logged out: userId={}", userId);
  }

  private RefreshToken createRefreshToken(AuthUser user) {
    RefreshToken refreshToken = RefreshToken.builder()
        .user(user)
        .token(UUID.randomUUID().toString())
        .expiresAt(Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpiration()))
        .createdAt(Instant.now())
        .revoked(false)
        .build();
    return refreshTokenRepository.save(refreshToken);
  }

  private AuthResponse buildAuthResponse(AuthUser user, String accessToken, String refreshToken) {
    return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .tokenType("Bearer")
        .expiresIn(jwtTokenProvider.getAccessTokenExpiration())
        .userId(user.getId())
        .email(user.getEmail())
        .role(user.getRole().name())
        .build();
  }
}
