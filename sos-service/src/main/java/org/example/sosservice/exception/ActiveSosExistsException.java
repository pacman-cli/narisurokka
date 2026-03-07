package org.example.sosservice.exception;

public class ActiveSosExistsException extends RuntimeException {
  public ActiveSosExistsException(String message) {
    super(message);
  }
}
