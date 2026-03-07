package org.example.sosservice.exception;

public class SosNotFoundException extends RuntimeException {
  public SosNotFoundException(String message) {
    super(message);
  }
}
