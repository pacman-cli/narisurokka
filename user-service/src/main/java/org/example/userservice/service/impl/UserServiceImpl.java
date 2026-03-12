package org.example.userservice.service.impl;

import java.time.Instant;
import java.util.UUID;

import org.example.userservice.dto.UserPreferenceRequest;
import org.example.userservice.dto.UserPreferenceResponse;
import org.example.userservice.dto.UserRegistrationRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.dto.UserUpdateRequest;
import org.example.userservice.event.UserRegisteredEvent;
import org.example.userservice.exception.DuplicateUserException;
import org.example.userservice.exception.UserNotFoundException;
import org.example.userservice.kafka.UserEventProducer;
import org.example.userservice.model.Gender;
import org.example.userservice.model.User;
import org.example.userservice.model.UserPreference;
import org.example.userservice.repository.UserPreferenceRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl {

  private final UserRepository userRepository;
  private final UserPreferenceRepository preferenceRepository;
  private final UserEventProducer eventProducer;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional
  public UserResponse registerUser(UserRegistrationRequest request) {
    // Check for duplicate email
    if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
      throw new DuplicateUserException("User with email " + request.getEmail() + " already exists");
    }

    // Check for duplicate phone
    if (userRepository.existsByPhoneAndDeletedAtIsNull(request.getPhone())) {
      throw new DuplicateUserException("User with phone " + request.getPhone() + " already exists");
    }

    // Build user entity
    User user = User.builder()
        .fullName(request.getFullName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .dateOfBirth(request.getDateOfBirth())
        .profilePhoto(request.getProfilePhoto())
        .gender(parseGender(request.getGender()))
        .bloodGroup(request.getBloodGroup())
        .address(request.getAddress())
        .build();

    user = userRepository.save(user);

    // Create default preferences
    UserPreference preference = UserPreference.builder()
        .user(user)
        .build();
    preferenceRepository.save(preference);

    // Publish Kafka event
    // eventProducer.publishUserRegistered(user.getId(), user.getEmail(),
    // user.getPhone());
    // bug fix: instead of calling kafka direclty ,publish a spring event
    applicationEventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getEmail(), user.getPhone()));

    log.info("User registered successfully: id={}, email={}", user.getId(), user.getEmail());
    return mapToResponse(user);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(UUID id) {
    User user = findActiveUserById(id);
    return mapToResponse(user);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserByPhone(String phone) {
    User user = userRepository.findByPhoneAndDeletedAtIsNull(phone)
        .orElseThrow(() -> new UserNotFoundException("User with phone " + phone + " not found"));
    return mapToResponse(user);
  }

  @Transactional
  public UserResponse updateUser(UUID id, UserUpdateRequest request) {
    User user = findActiveUserById(id);

    // Update only non-null fields (partial update)
    if (request.getFullName() != null) {
      user.setFullName(request.getFullName());
    }
    if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
      if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
        throw new DuplicateUserException("Email " + request.getEmail() + " is already in use");
      }
      user.setEmail(request.getEmail());
    }
    if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
      if (userRepository.existsByPhoneAndDeletedAtIsNull(request.getPhone())) {
        throw new DuplicateUserException("Phone " + request.getPhone() + " is already in use");
      }
      user.setPhone(request.getPhone());
    }
    if (request.getDateOfBirth() != null) {
      user.setDateOfBirth(request.getDateOfBirth());
    }
    if (request.getProfilePhoto() != null) {
      user.setProfilePhoto(request.getProfilePhoto());
    }
    if (request.getGender() != null) {
      user.setGender(parseGender(request.getGender()));
    }
    if (request.getBloodGroup() != null) {
      user.setBloodGroup(request.getBloodGroup());
    }
    if (request.getAddress() != null) {
      user.setAddress(request.getAddress());
    }

    user = userRepository.save(user);

    // Publish Kafka event
    eventProducer.publishProfileUpdated(user.getId());

    log.info("User profile updated: id={}", user.getId());
    return mapToResponse(user);
  }

  @Transactional
  public void deleteUser(UUID id) {
    User user = findActiveUserById(id);

    // Soft delete
    user.setIsActive(false);
    user.setDeletedAt(Instant.now());
    userRepository.save(user);

    // Publish Kafka event
    eventProducer.publishUserDeleted(user.getId());

    log.info("User soft-deleted: id={}", user.getId());
  }

  @Transactional(readOnly = true)
  public UserPreferenceResponse getUserPreferences(UUID userId) {
    // Verify user exists
    findActiveUserById(userId);

    UserPreference preference = preferenceRepository.findByUserId(userId)
        .orElseThrow(() -> new UserNotFoundException("Preferences not found for user " + userId));

    return mapToPreferenceResponse(preference);
  }

  @Transactional
  public UserPreferenceResponse updateUserPreferences(UUID userId, UserPreferenceRequest request) {
    // Verify user exists
    findActiveUserById(userId);

    UserPreference preference = preferenceRepository.findByUserId(userId)
        .orElseThrow(() -> new UserNotFoundException("Preferences not found for user " + userId));

    // Update only non-null fields
    if (request.getEnableLiveTracking() != null) {
      preference.setEnableLiveTracking(request.getEnableLiveTracking());
    }
    if (request.getSosShakeTrigger() != null) {
      preference.setSosShakeTrigger(request.getSosShakeTrigger());
    }
    if (request.getNotificationSms() != null) {
      preference.setNotificationSms(request.getNotificationSms());
    }
    if (request.getNotificationEmail() != null) {
      preference.setNotificationEmail(request.getNotificationEmail());
    }
    if (request.getNotificationPush() != null) {
      preference.setNotificationPush(request.getNotificationPush());
    }
    if (request.getLanguage() != null) {
      preference.setLanguage(request.getLanguage());
    }

    preference = preferenceRepository.save(preference);

    // Publish profile updated event (preferences are part of profile)
    eventProducer.publishProfileUpdated(userId);

    log.info("User preferences updated: userId={}", userId);
    return mapToPreferenceResponse(preference);
  }

  // ===================== Helper Methods =====================

  private User findActiveUserById(UUID id) {
    return userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
  }

  private Gender parseGender(String gender) {
    if (gender == null || gender.isBlank()) {
      return Gender.FEMALE;
    }
    try {
      return Gender.valueOf(gender.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid gender value: " + gender
          + ". Allowed values: FEMALE, MALE, OTHER, PREFER_NOT_TO_SAY");
    }
  }

  private UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .dateOfBirth(user.getDateOfBirth())
        .profilePhoto(user.getProfilePhoto())
        .gender(user.getGender() != null ? user.getGender().name() : null)
        .bloodGroup(user.getBloodGroup())
        .address(user.getAddress())
        .isActive(user.getIsActive())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

  private UserPreferenceResponse mapToPreferenceResponse(UserPreference pref) {
    return UserPreferenceResponse.builder()
        .id(pref.getId())
        .userId(pref.getUser().getId())
        .enableLiveTracking(pref.getEnableLiveTracking())
        .sosShakeTrigger(pref.getSosShakeTrigger())
        .notificationSms(pref.getNotificationSms())
        .notificationEmail(pref.getNotificationEmail())
        .notificationPush(pref.getNotificationPush())
        .language(pref.getLanguage())
        .createdAt(pref.getCreatedAt())
        .updatedAt(pref.getUpdatedAt())
        .build();
  }
}
