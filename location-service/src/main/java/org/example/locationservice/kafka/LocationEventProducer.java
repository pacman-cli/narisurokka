package org.example.locationservice.kafka;

import lombok.RequiredArgsConstructor;
import org.example.locationservice.model.LocationPing;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final static String TOPIC="location.update";

    public void publishLocation(LocationPing ping){
        kafkaTemplate.send(TOPIC,ping.getSosId().toString(),ping);
    }
}
