package com.example.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final String TOPIC_NAME = "unit";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void produceMessage(String message) {
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
