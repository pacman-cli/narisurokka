package org.example.userservice.event;

import org.example.userservice.kafka.UserEventProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

//This guarantees Kafka events are only sent after the database transaction successfully commits
@Component
@RequiredArgsConstructor
public class UserEventListener {

  private final UserEventProducer eventProducer;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onUserRegistered(UserRegisteredEvent event) {
    eventProducer.publishUserRegistered(event.userId(), event.email(), event.phone());
  }
}
