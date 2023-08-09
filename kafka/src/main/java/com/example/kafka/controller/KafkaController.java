package com.example.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private static final String TOPIC_NAME = "unit";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/message")
    public String sendMessage(@RequestBody String message) {
        kafkaTemplate.send(TOPIC_NAME, message);
        return "Message sent: " + message;
    }
}
