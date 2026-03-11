package org.example.locationservice.exception;

public class LocationNotFoundException extends RuntimeException {
  public LocationNotFoundException(String message) {
    super(message);
  }
}
