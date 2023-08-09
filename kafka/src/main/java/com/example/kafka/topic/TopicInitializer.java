package com.example.kafka.topic;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TopicInitializer {

    private static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_NAME = "unit";
    private static final int NUM_PARTITIONS = 1;
    private static final short REPLICATION_FACTOR = 1;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic unitTopic() {
        return new NewTopic(TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }
}
