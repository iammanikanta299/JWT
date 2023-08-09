package com.example.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final String TOPIC_NAME = "unit";

    @KafkaListener(topics = TOPIC_NAME, groupId = "kafka-consumer-group")
    public void consumeMessage(String message) {
        // Process the received message
        System.out.println("Received message: " + message);

        // Perform database operations or any other necessary processing
    }
}
