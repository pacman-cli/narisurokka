package org.example.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SosEventConsumer {
    @KafkaListener(topics = "sos-event")
    public void consume(){

    }

    private void handleSosTriggered(){

    }

    private void handleSosCancelled(){

    }
}
